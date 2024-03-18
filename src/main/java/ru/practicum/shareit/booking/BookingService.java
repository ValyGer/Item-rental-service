package ru.practicum.shareit.booking;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking setApprovedByOwner(long userId, long bookingId, Boolean approved);

    Booking getBookingById(long userId, long bookingId);
}
