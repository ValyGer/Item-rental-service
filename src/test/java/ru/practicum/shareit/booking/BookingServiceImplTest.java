package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.model.Booking;


import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingDtoWithItemMapper bookingDtoWithItemMapper;



    @Test
    void createBooking() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User booker1 = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker2 = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker2, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        when(userService.getUserById(booking.getBooker().getId())).thenReturn(booker2);
        when(itemService.getItemsById(booking.getItem().getItemId())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking bookingSaved = bookingService.createBooking(booking);

        assertThat(bookingSaved.getBookingId(), equalTo(booking.getBookingId()));
        assertThat(bookingSaved.getItem().getItemId(), equalTo((booking.getItem().getItemId())));
        assertThat(bookingSaved.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(bookingSaved.getStart(), equalTo(booking.getStart()));
        assertThat(bookingSaved.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void setApprovedByOwner() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getAllBookingByUser() {
    }

    @Test
    void getAllBookingByOwner() {
    }

    @Test
    void getAllBookingForItemByUser() {
    }
}