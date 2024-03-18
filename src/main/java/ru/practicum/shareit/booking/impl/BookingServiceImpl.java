package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.State.PAST;

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
                    throw new ConflictException("Пользователь не может арендовать у себя");
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
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            if (booking.isPresent()) {
                // проверка на то, что пользователь является владельцем вещи
                if (booking.get().getItem().getOwner() == userId) {
                    // проверка статуса бронирования
                    if (approved) {
                        if (booking.get().getStatus().equals(Status.APPROVED)) {
                            log.info("Бронирование уже подтверждено, при необходимости можно отменить его");
                            throw new ValidationException("Бронирование уже подтверждено, при необходимости можно отменить его");
                        } else {
                            booking.get().setStatus(Status.APPROVED);
                            log.info("Бронирование успешно подтверждено одобрено");
                            return booking.get();
                        }
                    } else {
                        if (booking.get().getStatus().equals(Status.REJECTED)) {
                            log.info("Бронирование отменено, изменение статуса повторно не возможно");
                            throw new ValidationException("Бронирование отменено, изменение статуса повторно не возможно");
                        } else {
                            booking.get().setStatus(Status.REJECTED);
                            log.info("Бронирование успешно отменено");
                            return booking.get();
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


    // Получение всех бронирований пользователя
    public List<Booking> getAllBookingByUser(long userId, String state) {
        State stateOfBooking;
        try {
            stateOfBooking = State.valueOf(state);
        } catch (IllegalArgumentException ex) {
            log.info("Введенный статус не доступен");
            throw new NotFoundException("Введенный адрес не доступен");
        }


        List<Booking> listOfBooking = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {

            switch (stateOfBooking) {
                case All: {
                    listOfBooking = bookingRepositoryJpa.findAllByItem_OwnerOrderByStartTimeDesc(bookerFromDb);
                    break;
                }
                case CURRENT: {
                    listOfBooking = bookingRepositoryJpa.findAllBookingsItemByForOwnerWithStartAndEndTime(
                            bookerFromDb, nowDateTime, nowDateTime);
                    break;
                }
                case PAST: {
                    listOfBooking = bookingRepositoryJpa.findAllByItem_OwnerAndEndTimeIsBeforeOrderByStartTimeDesc(
                            bookerFromDb, nowDateTime);
                    break;
                }
                case FUTURE: {
                    listOfBooking = bookingRepositoryJpa.findAllByItem_OwnerAndStartTimeIsAfterOrderByStartTimeDesc(
                            bookerFromDb, nowDateTime);
                    break;
                }
                case WAITING: {
                    listOfBooking = bookingRepositoryJpa.findAllByItem_OwnerAndBookingStatusEqualsOrderByStartTimeDesc(
                            bookerFromDb, BookingStatus.WAITING);
                    break;
                }
                case REJECTED: {
                    listOfBooking = bookingRepositoryJpa.findAllByItem_OwnerAndBookingStatusEqualsOrderByStartTimeDesc(
                            bookerFromDb, BookingStatus.REJECTED);
                    break;
                }
                default: {
                    throw new NotFoundException("Unknown state: UNSUPPORTED_STATUS");
                }
            }
            return result.stream()
                    .map(bookingForResponseMapper::mapToDto).collect(Collectors.toList());
        }






        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
    }
}

}
