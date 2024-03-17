package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

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

    public Booking getBookingById(long userId, long bookingId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            if (booking.isPresent()) {
                if (booking.get().getBooker().getId() == userId) {
                    return booking.get();
                } else {
                    log.info("Бронирование не принадлежит пользователю с Id = {}", userId);
                    throw new NotFoundException("Бронирование не принадлежит пользователю");
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
}
