package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingLastNextDtoMapper {
    @Mapping(source = "bookingId", target = "id")
    @Mapping(source = "booker.id", target = "bookerId")
    BookingLastNextDto toBookingLastNextDto(Booking booking);
}
