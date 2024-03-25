package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    // Добавление нового бронирования
    public Booking createBooking(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            log.info("Время начала бронирования указано после окончания бронирования или равно ему");
            throw new ValidationException("Время начала бронирования указано после окончания бронирования или равно ему");
        }
        Optional<User> user = userRepository.findById(booking.getBooker().getId());
        Optional<Item> item = itemRepository.findById(booking.getItem().getItemId());
        if (user.isPresent()) {
            if (item.isPresent()) {
                if (booking.getBooker().getId() != item.get().getOwner()) {
                    if (item.get().getIsAvailable()) {
                        booking.setBooker(user.get());
                        booking.setItem(item.get());
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
            } else {
                log.info("Вещь с Id = {} не существует в базе", booking.getItem().getItemId());
                throw new NotFoundException("Пользователь не найден");
            }
        } else {
            log.info("Пользователь с Id = {} не существует в базе", booking.getBooker().getId());
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Подтверждение или отклонении бронирования
    public Booking setApprovedByOwner(long userId, long bookingId, Boolean approved) {
        // проверка пользователя бронирования
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // проверка наличия бронирования
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
            if (bookingOpt.isPresent()) {
                // проверка на то, что пользователь является владельцем вещи
                if (bookingOpt.get().getItem().getOwner() == userId) {
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
        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Получение информации о бронировании
    public Booking getBookingById(long userId, long bookingId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            if (booking.isPresent()) {
                if ((booking.get().getBooker().getId() == userId) || (booking.get().getItem().getOwner() == userId)) {
                    return booking.get();
                } else {
                    log.info("Бронирование или вещь не принадлежит пользователю с Id = {}", userId);
                    throw new NotFoundException("Бронирование или вещь не принадлежит пользователю");
                }
            } else {
                log.info("Бронирование Id = {} не найдено", bookingId);
                throw new NotFoundException("Бронирование не найдено");
            }
        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Получение списка всех бронирований текущего пользователя
    public List<Booking> getAllBookingByUser(int from, int size, long userId, String state) {
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
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            switch (stateOfBooking) {
                case ALL: {
                    listOfBooking = bookingRepository.findBookingsByBooker_IdOrderByStartDesc(user.get().getId(), pageRequest);
                    break;
                }
                case CURRENT: {
                    listOfBooking = bookingRepository.findAllBookingsForBookerWithStartAndEnd(
                            user.get().getId(), LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                    break;
                }
                case PAST: {
                    listOfBooking = bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(
                            user.get().getId(), LocalDateTime.now(), pageRequest);
                    break;
                }
                case FUTURE: {
                    listOfBooking = bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(
                            user.get().getId(), LocalDateTime.now(), pageRequest);
                    break;
                }
                case WAITING: {
                    listOfBooking = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(
                            user.get().getId(), Status.WAITING, pageRequest);
                    break;
                }
                case REJECTED: {
                    listOfBooking = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(
                            user.get().getId(), Status.REJECTED, pageRequest);
                    break;
                }
            }
            return listOfBooking;
        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
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

        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isPresent()) {
            switch (stateOfBooking) {
                case ALL: {
                    listOfBooking = bookingRepository.findAllByItem_OwnerOrderByStartDesc(owner.get().getId(), pageRequest);
                    break;
                }
                case CURRENT: {
                    listOfBooking = bookingRepository.findAllByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                            owner.get().getId(), LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                    break;
                }
                case PAST: {
                    listOfBooking = bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(
                            owner.get().getId(), LocalDateTime.now(), pageRequest);
                    break;
                }
                case FUTURE: {
                    listOfBooking = bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                            owner.get().getId(), LocalDateTime.now(), pageRequest);
                    break;
                }
                case WAITING: {
                    listOfBooking = bookingRepository.findAllByItem_OwnerAndStatusOrderByStartDesc(
                            owner.get().getId(), Status.WAITING, pageRequest);
                    break;
                }
                case REJECTED: {
                    listOfBooking = bookingRepository.findAllByItem_OwnerAndStatusOrderByStartDesc(
                            owner.get().getId(), Status.REJECTED, pageRequest);
                    break;
                }
            }
            return listOfBooking;
        } else {
            log.info("Пользователь с Id = {} не существует в базе", ownerId);
            throw new NotFoundException("Пользователь не найден");
        }
    }
}