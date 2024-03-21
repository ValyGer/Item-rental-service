package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking setApprovedByOwner(long userId, long bookingId, Boolean approved);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getAllBookingByUser(long userId, String state);

    List<Booking> getAllBookingByOwner(long userId, String state);
}
