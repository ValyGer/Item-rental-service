package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class BookingLastNextDtoMapperTest {

    @InjectMocks
    BookingLastNextDtoMapperImpl bookingLastNextDtoMapper;

    @Test
    void toBookingLastNextDto() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        BookingLastNextDto bookingLastNextDto = bookingLastNextDtoMapper.toBookingLastNextDto(booking);

        assertEquals(booking.getBookingId(), bookingLastNextDto.getId());
        assertEquals(booking.getBooker().getId(), bookingLastNextDto.getBookerId());
    }

    @Test
    void toBookingLastNextDtoWhenNull() {
        Booking booking = null;
        BookingLastNextDto bookingLastNextDto = bookingLastNextDtoMapper.toBookingLastNextDto(booking);

        assertNull(bookingLastNextDto);
    }
}