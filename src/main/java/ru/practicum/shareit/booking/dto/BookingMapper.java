package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "bookingId", target = "id")
    @Mapping(source = "booker.id", target = "bookerId")
    @Mapping(source = "item.itemId", target = "itemId")
    BookingDto toBookingDto(Booking booking);

    @Mapping(source = "id", target = "bookingId")
    @Mapping(source = "bookerId", target = "booker.id")
    @Mapping(source = "itemId", target = "item.itemId")
    Booking toBooking(BookingDto bookingDto);
}
