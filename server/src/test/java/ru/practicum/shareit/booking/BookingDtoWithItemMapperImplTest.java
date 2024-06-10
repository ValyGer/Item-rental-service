package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class BookingDtoWithItemMapperImplTest {

    @InjectMocks
    BookingDtoWithItemMapperImpl bookingDtoWithItemMapper;

    @Test
    void toBookingDtoWithItemTest() {
        Item item = new Item(1L, "Name", "About of item", new User(), true);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        BookingDtoWithItem bookingDtoWithItem = bookingDtoWithItemMapper.toBookingDtoWithItem(booking);

        assertEquals(booking.getBookingId(), bookingDtoWithItem.getId());
        assertEquals(booking.getItem().getItemId(), bookingDtoWithItem.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDtoWithItem.getBooker().getId());
        assertEquals(booking.getStart(), bookingDtoWithItem.getStart());
        assertEquals(booking.getEnd(), bookingDtoWithItem.getEnd());
    }

    @Test
    void toBookingDtoWithItemTestNull() {
        Booking booking = null;
        BookingDtoWithItem bookingDtoWithItem = bookingDtoWithItemMapper.toBookingDtoWithItemNotTime(booking);

        assertNull(bookingDtoWithItem);
    }

    @Test
    void toBookingDtoWithItemNotTimeTest() {
        Item item = new Item(1L, "Name", "About of item", new User(), true);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        BookingDtoWithItem bookingDtoWithItem = bookingDtoWithItemMapper.toBookingDtoWithItemNotTime(booking);

        assertEquals(booking.getBookingId(), bookingDtoWithItem.getId());
        assertEquals(booking.getItem().getItemId(), bookingDtoWithItem.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDtoWithItem.getBooker().getId());
        assertEquals(booking.getStart(), bookingDtoWithItem.getStart());
        assertEquals(booking.getEnd(), bookingDtoWithItem.getEnd());
    }
}