package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
    void createBooking_whenCreateIsSuccess() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        when(userService.getUserById(booking.getBooker().getId())).thenReturn(booker);
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
    void setApprovedByOwner_isGood() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));
        booking.setStatus(Status.WAITING);

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking bookingSaved = bookingService.setApprovedByOwner(owner.getId(), booking.getBookingId(), true);

        assertThat(bookingSaved.getBookingId(), equalTo(booking.getBookingId()));
        assertThat(bookingSaved.getItem().getItemId(), equalTo((booking.getItem().getItemId())));
        assertThat(bookingSaved.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(bookingSaved.getStart(), equalTo(booking.getStart()));
        assertThat(bookingSaved.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingSaved.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void getBookingById_thenReturnBooking() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.of(booking));

        Booking bookingSaved = bookingService.getBookingById(owner.getId(), booking.getBookingId());

        assertThat(bookingSaved.getBookingId(), equalTo(booking.getBookingId()));
        assertThat(bookingSaved.getItem().getItemId(), equalTo((booking.getItem().getItemId())));
        assertThat(bookingSaved.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(bookingSaved.getStart(), equalTo(booking.getStart()));
        assertThat(bookingSaved.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingSaved.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void getBookingById_thenReturnThrow() {
        Item item = new Item(1L, "Name", "About of item", 1L, true);
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(owner.getId(), booking.getBookingId()));
    }

    @Test
    void getAllBookingByUser() {
    }

    @Test
    void getAllBookingByOwner() {
    }

    @Test
    void getAllBookingForItemBy() {

    }
}