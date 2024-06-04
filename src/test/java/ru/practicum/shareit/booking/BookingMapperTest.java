package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @InjectMocks
    private BookingMapperImpl bookingMapper;

    @Test
    void toBookingDto() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertEquals(booking.getBookingId(), bookingDto.getId());
        assertEquals(booking.getItem().getItemId(), bookingDto.getItemId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBookerId());
    }

    @Test
    void toBookingDtoNull() {
        Booking booking = null;
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertNull(bookingDto);
    }


    @Test
    void toBooking() {
        LocalDateTime time = LocalDateTime.now();

        BookingDto bookingDto = new BookingDto(1L, 2L, time.plusMinutes(10), time.plusMinutes(100));

        Booking booking = bookingMapper.toBooking(bookingDto);

        assertEquals(bookingDto.getItemId(), booking.getItem().getItemId());
        assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
    }

    @Test
    void toBookingNull() {
        BookingDto bookingDto = null;
        Booking booking = bookingMapper.toBooking(bookingDto);

        assertNull(booking);
    }
}