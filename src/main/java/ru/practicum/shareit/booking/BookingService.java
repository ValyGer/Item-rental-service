package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking setApprovedByOwner(long userId, long bookingId, Boolean approved);

    Booking getBookingById(long userId, long bookingId);

    List<BookingDtoWithItem> getAllBookingByUser(int from, int size, long userId, String state);

    List<Booking> getAllBookingByOwner(int from, int size, long ownerId, String state);

    List<Booking> getAllBookingByUser(Item item);

    List<Booking> getAllBookingForItemByUser(Item item, User user, LocalDateTime now);
}
