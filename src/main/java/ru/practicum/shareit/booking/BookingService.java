package ru.practicum.shareit.booking;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking getBookingById(long userId, long bookingId);
}
