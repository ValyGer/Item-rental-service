package ru.practicum.shareit.booking.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor(force = true)
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final BookingDtoWithItemMapper bookingDtoWithItemMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    @Lazy
    public BookingServiceImpl(ItemService itemService, UserService userService, BookingRepository bookingRepository,
                              BookingDtoWithItemMapper bookingDtoWithItemMapper, BookingMapper bookingMapper) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.bookingDtoWithItemMapper = bookingDtoWithItemMapper;
        this.bookingMapper = bookingMapper;
    }

    // Добавление нового бронирования
    public Booking createBooking(long bookerId, BookingDto bookingDto) {
        bookingDto.setBookerId(bookerId);
        Booking booking = bookingMapper.toBooking(bookingDto);
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            log.info("Время начала бронирования указано после окончания бронирования или равно ему");
            throw new ValidationException("Время начала бронирования указано после окончания бронирования или равно ему");
        }
        User user = userService.getUserById(booking.getBooker().getId());
        Item item = itemService.getItemsById(booking.getItem().getItemId());
        if (booking.getBooker().getId() != item.getOwner().getId()) {
            if (item.getIsAvailable()) {
                booking.setBooker(user);
                booking.setItem(item);
                booking.setStatus(Status.WAITING);
                log.info("Бронирование вещи с Id = {} создано и ожидает подтверждения", booking.getItem().getItemId());
                return bookingRepository.save(booking);
            } else {
                log.info("Бронирование вещи с Id = {} недоступно в данный момент", booking.getItem().getItemId());
                throw new ValidationException("Бронирование вещи с недоступно в данный момент");
            }
        } else {
            log.info("Пользователь не может арендовать у себя");
            throw new NotFoundException("Пользователь не может арендовать у себя");
        }
    }

    // Подтверждение или отклонении бронирования
    public Booking setApprovedByOwner(long userId, long bookingId, Boolean approved) {
        // проверка пользователя бронирования
        userService.getUserById(userId);
        // проверка наличия бронирования
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            // проверка на то, что пользователь является владельцем вещи
            if (bookingOpt.get().getItem().getOwner().getId() == userId) {
                // проверка статуса бронирования
                if (approved) {
                    if (bookingOpt.get().getStatus().equals(Status.APPROVED)) {
                        log.info("Бронирование уже подтверждено, при необходимости можно отменить его");
                        throw new ValidationException("Бронирование уже подтверждено, при необходимости можно отменить его");
                    } else {
                        Booking booking = bookingOpt.get();
                        booking.setStatus(Status.APPROVED);
                        log.info("Бронирование успешно подтверждено одобрено");
                        return bookingRepository.save(booking);
                    }
                } else {
                    if (bookingOpt.get().getStatus().equals(Status.REJECTED)) {
                        log.info("Бронирование отменено, изменение статуса повторно не возможно");
                        throw new ValidationException("Бронирование отменено, изменение статуса повторно не возможно");
                    } else {
                        Booking booking = bookingOpt.get();
                        booking.setStatus(Status.REJECTED);
                        log.info("Бронирование успешно отменено");
                        return bookingRepository.save(booking);
                    }
                }
            } else {
                log.info("Забронированная вещь не принадлежит пользователю, желающему внести изменения");
                throw new NotFoundException("Забронированная вещь не принадлежит пользователю, желающему " +
                        "внести изменения");
            }
        } else {
            log.info("Бронирование Id = {} не найдено", bookingId);
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    // Получение информации о бронировании
    public Booking getBookingById(long userId, long bookingId) {
        userService.getUserById(userId);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            if ((booking.get().getBooker().getId() == userId) || (booking.get().getItem().getOwner().getId() == userId)) {
                return booking.get();
            } else {
                log.info("Бронирование или вещь не принадлежит пользователю с Id = {}", userId);
                throw new NotFoundException("Бронирование или вещь не принадлежит пользователю");
            }
        } else {
            log.info("Бронирование Id = {} не найдено", bookingId);
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    // Получение списка всех бронирований текущего пользователя
    public List<BookingDtoWithItem> getAllBookingByUser(int from, int size, long userId, String state) {
        // проверяем отрицательный ли параметр
        if (from < 0) {
            throw new ValidationException("Значение не может быть отрицательным");
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        // Проверка параметра state
        State stateOfBooking;
        if (state.isBlank()) {
            stateOfBooking = State.ALL;
        } else {
            try {
                stateOfBooking = State.valueOf(state.toUpperCase());
            } catch (RuntimeException ex) {
                log.info("Введенный статус не существует");
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        List<Booking> listOfBooking;
        List<BookingDtoWithItem> listOfBookingDto = new ArrayList<>();
        User user = userService.getUserById(userId);
        switch (stateOfBooking) {
            case ALL: {
                listOfBooking = bookingRepository.findBookingsByBooker_IdOrderByStartDesc(user.getId(), pageRequest);
                listOfBookingDto = listOfBooking.stream()
                        .map(bookingDtoWithItemMapper::toBookingDtoWithItem)
                        .collect(Collectors.toList());
                break;
            }
            case CURRENT: {
                listOfBooking = bookingRepository.findAllBookingsForBooker_IdWithStartAndEnd(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                listOfBookingDto = listOfBooking.stream()
                        .map(bookingDtoWithItemMapper::toBookingDtoWithItemNotTime)
                        .collect(Collectors.toList());
                break;
            }
            case PAST: {
                listOfBooking = bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(
                        user.getId(), LocalDateTime.now(), pageRequest);
                listOfBookingDto = listOfBooking.stream()
                        .map(bookingDtoWithItemMapper::toBookingDtoWithItem)
                        .collect(Collectors.toList());
                break;
            }
            case FUTURE: {
                listOfBooking = bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(
                        user.getId(), LocalDateTime.now(), pageRequest);
                listOfBookingDto = listOfBooking.stream()
                        .map(bookingDtoWithItemMapper::toBookingDtoWithItem)
                        .collect(Collectors.toList());
                break;
            }
            case WAITING: {
                listOfBooking = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(
                        user.getId(), Status.WAITING, pageRequest);
                listOfBookingDto = listOfBooking.stream()
                        .map(bookingDtoWithItemMapper::toBookingDtoWithItem)
                        .collect(Collectors.toList());
                break;
            }
            case REJECTED: {
                listOfBooking = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(
                        user.getId(), Status.REJECTED, pageRequest);
                listOfBookingDto = listOfBooking.stream()
                        .map(bookingDtoWithItemMapper::toBookingDtoWithItem)
                        .collect(Collectors.toList());
                break;
            }
        }
        return listOfBookingDto;
    }

    // Получение списка бронирований для всех вещей текущего пользователя
    public List<Booking> getAllBookingByOwner(int from, int size, long ownerId, String state) {
        // проверяем отрицательный ли параметр
        if (from < 0) {
            throw new ValidationException("Значение не может быть отрицательным");
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        // Проверка параметра state
        State stateOfBooking;
        if (state.isBlank()) {
            stateOfBooking = State.ALL;
        } else {
            try {
                stateOfBooking = State.valueOf(state.toUpperCase());
            } catch (RuntimeException ex) {
                log.info("Введенный статус не существует");
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
        List<Booking> listOfBooking = new ArrayList<>();
        User owner = userService.getUserById(ownerId);
        switch (stateOfBooking) {
            case ALL: {
                listOfBooking = bookingRepository.findAllByItem_OwnerOrderByStartDesc(owner, pageRequest);
                break;
            }
            case CURRENT: {
                listOfBooking = bookingRepository.findAllByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        owner, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                break;
            }
            case PAST: {
                listOfBooking = bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(
                        owner, LocalDateTime.now(), pageRequest);
                break;
            }
            case FUTURE: {
                listOfBooking = bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                        owner, LocalDateTime.now(), pageRequest);
                break;
            }
            case WAITING: {
                listOfBooking = bookingRepository.findAllByItem_OwnerAndStatusOrderByStartDesc(
                        owner, Status.WAITING, pageRequest);
                break;
            }
            case REJECTED: {
                listOfBooking = bookingRepository.findAllByItem_OwnerAndStatusOrderByStartDesc(
                        owner, Status.REJECTED, pageRequest);
                break;
            }
        }
        return listOfBooking;
    }

    // Получение списка всех бронирований для данной вещи
    public List<Booking> getAllBookingByUser(Item item) {
        return bookingRepository.findAllByItemOrderByStartDesc(item);
    }

    // Получение всех бронирований для данной вещи данным пользователем до настоящего времени
    public List<Booking> getAllBookingForItemByUser(Item item, User user, LocalDateTime now) {
        return bookingRepository.findByBookingByItemAndBookerAndEndBefore(item, user, now);
    }
}