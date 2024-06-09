package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private BookingMapper bookingMapper;

    @Mock
    private BookingDtoWithItemMapper bookingDtoWithItemMapper;

    @Test
    void createBooking_whenCreateIsSuccess() {
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item(1L, "Name", "About of item", new User(), true);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, time.plusMinutes(-10L), time.plusMinutes(10L));
        BookingDto bookingDto = new BookingDto();

        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(booking);
        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(itemService.getItemsById(any(Long.class))).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking bookingSaved = bookingService.createBooking(booker.getId(), bookingDto);

        assertThat(bookingSaved.getBookingId(), equalTo(booking.getBookingId()));
        assertThat(bookingSaved.getItem().getItemId(), equalTo((booking.getItem().getItemId())));
        assertThat(bookingSaved.getBooker().getId(), equalTo(booking.getBooker().getId()));
        assertThat(bookingSaved.getStart(), equalTo(booking.getStart()));
        assertThat(bookingSaved.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void createBooking_whenThrowValidation() {
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item(1L, "Name", "About of item", new User(), true);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, time.plusMinutes(10L), time.plusMinutes(-10L));
        BookingDto bookingDto = new BookingDto();
        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(booking);

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(booker.getId(), bookingDto));
    }

    @Test
    void createBooking_whenUserNotFound() {
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item(1L, "Name", "About of item", new User(), true);
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        Booking booking = new Booking(1L, item, owner, time.plusMinutes(-10L), time.plusMinutes(10L));
        BookingDto bookingDto = new BookingDto();
        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(booking);
        when(userService.getUserById(any(Long.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(owner.getId(), bookingDto));
    }

    @Test
    void createBooking_whenBookingItemNotAvailable() {
        LocalDateTime time = LocalDateTime.now();
        Item item = new Item(1L, "Name", "About of item", new User(), false);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Booking booking = new Booking(1L, item, booker, time.plusMinutes(-10L), time.plusMinutes(10L));
        BookingDto bookingDto = new BookingDto();
        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(booking);
        when(itemService.getItemsById(any(Long.class))).thenReturn(item);

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(booker.getId(), bookingDto));
    }


    @Test
    void setApprovedByOwner_isGood() {
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Item item = new Item(1L, "Name", "About of item", owner, true);
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L),
                LocalDateTime.now().plusMinutes(10L));
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
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Item item = new Item(1L, "Name", "About of item", owner, true);
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
        User owner = new User(1L, "Name", "user@mail.ru"); // владелиц вещи
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        Item item = new Item(1L, "Name", "About of item", owner, true);
        Booking booking = new Booking(1L, item, booker, LocalDateTime.now().plusMinutes(-10L), LocalDateTime.now().plusMinutes(10L));

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(owner.getId(), booking.getBookingId()));
    }

    @Test
    void getAllBookingByUser_whenStateAll() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(any(Long.class), any(PageRequest.class)))
                .thenReturn(listOfBooking);

        bookingService.getAllBookingByUser(0, 20, booker.getId(), State.ALL.toString());

        verify(bookingRepository, times(1))
                .findBookingsByBooker_IdOrderByStartDesc(booker.getId(), pageRequest);
    }

    @Test
    void getAllBookingByUser_whenStateCURRENT() {
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findAllBookingsForBooker_IdWithStartAndEnd(any(Long.class), any(LocalDateTime.class),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByUser(0, 20, booker.getId(), State.CURRENT.toString());

        verify(bookingRepository, times(1))
                .findAllBookingsForBooker_IdWithStartAndEnd(any(Long.class), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByUser_whenStatePAST() {
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(any(Long.class),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByUser(0, 20, booker.getId(), State.PAST.toString());

        verify(bookingRepository, times(1))
                .findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(any(Long.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByUser_whenStateFUTURE() {
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(any(Long.class),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByUser(0, 20, booker.getId(), State.FUTURE.toString());

        verify(bookingRepository, times(1))
                .findAllByBooker_IdAndStartIsAfterOrderByStartDesc(any(Long.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByUser_whenStateWAITING() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(any(Long.class),
                any(Status.class), any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByUser(0, 20, booker.getId(), State.WAITING.toString());

        verify(bookingRepository, times(1))
                .findAllByBooker_IdAndStatusOrderByStartDesc(booker.getId(), Status.WAITING, pageRequest);
    }

    @Test
    void getAllBookingByUser_whenStateREJECTED() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User booker = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(any(Long.class),
                any(Status.class), any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByUser(0, 20, booker.getId(), State.REJECTED.toString());

        verify(bookingRepository, times(1))
                .findAllByBooker_IdAndStatusOrderByStartDesc(booker.getId(), Status.REJECTED, pageRequest);
    }

    @Test
    void getAllBookingByUser_whenFromNegative() {
        int form = -5;
        int size = 20;
        User user = new User(2L, "Name2", "mail@mail.ru"); // арендатор

        assertThrows(ValidationException.class,
                () -> bookingService.getAllBookingByUser(form, size, user.getId(), State.ALL.toString()));
    }

    @Test
    void getAllBookingByUser_whenNotKnowState() {
        int form = 1;
        int size = 20;
        User user = new User(2L, "Name2", "mail@mail.ru"); // арендатор

        assertThrows(RuntimeException.class,
                () -> bookingService.getAllBookingByUser(form, size, user.getId(), "Name"));
    }


    @Test
    void getAllBookingByOwner_whenStateALL() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User owner = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(owner);
        when(bookingRepository.findAllByItem_OwnerOrderByStartDesc(any(User.class), any(PageRequest.class)))
                .thenReturn(listOfBooking);

        bookingService.getAllBookingByOwner(0, 20, owner.getId(), State.ALL.toString());

        verify(bookingRepository, times(1))
                .findAllByItem_OwnerOrderByStartDesc(owner, pageRequest);
    }

    @Test
    void getAllBookingByOwner_whenStateCURRENT() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User owner = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();

        when(userService.getUserById(any(Long.class))).thenReturn(owner);
        when(bookingRepository.findAllByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(any(User.class),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByOwner(0, 20, owner.getId(), State.CURRENT.toString());

        verify(bookingRepository, times(1))
                .findAllByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByOwner_whenStatePAST() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User owner = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(owner);
        when(bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class),
                any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByOwner(0, 20, owner.getId(), State.PAST.toString());

        verify(bookingRepository, times(1))
                .findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByOwner_whenStateFUTURE() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User owner = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(owner);
        when(bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class),
                any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByOwner(0, 20, owner.getId(), State.FUTURE.toString());

        verify(bookingRepository, times(1))
                .findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByOwner_whenStateWAITING() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User owner = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(owner);
        when(bookingRepository.findAllByItem_OwnerAndStatusOrderByStartDesc(any(User.class), any(Status.class),
                any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByOwner(0, 20, owner.getId(), State.WAITING.toString());

        verify(bookingRepository, times(1))
                .findAllByItem_OwnerAndStatusOrderByStartDesc(owner, Status.WAITING, pageRequest);
    }

    @Test
    void getAllBookingByOwner_whenStateREJECTED() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        User owner = new User(2L, "Name2", "mail@mail.ru"); // арендатор
        List<Booking> listOfBooking = new ArrayList<>();

        when(userService.getUserById(any(Long.class))).thenReturn(owner);
        when(bookingRepository.findAllByItem_OwnerAndStatusOrderByStartDesc(any(User.class), any(Status.class),
                any(PageRequest.class))).thenReturn(listOfBooking);

        bookingService.getAllBookingByOwner(0, 20, owner.getId(), State.REJECTED.toString());

        verify(bookingRepository, times(1))
                .findAllByItem_OwnerAndStatusOrderByStartDesc(owner, Status.REJECTED, pageRequest);
    }

    @Test
    void getAllBookingByOwner_whenFromNegative() {
        int form = -5;
        int size = 20;
        User user = new User(2L, "Name2", "mail@mail.ru"); // арендатор

        assertThrows(ValidationException.class,
                () -> bookingService.getAllBookingByOwner(form, size, user.getId(), State.ALL.toString()));
    }

    @Test
    void getAllBookingByOwner_whenNotKnowState() {
        int form = 1;
        int size = 20;
        User user = new User(2L, "Name2", "mail@mail.ru"); // арендатор

        assertThrows(RuntimeException.class,
                () -> bookingService.getAllBookingByOwner(form, size, user.getId(), "Name"));
    }

    @Test
    void getAllBookingByUser_ReturnItem() {
        User owner = new User(1L, "Name", "user@mail.ru");
        Item item = new Item(1L, "Name", "About of item", owner, true);
        List<Booking> listOfBooking = new ArrayList<>();

        when(bookingRepository.findAllByItemOrderByStartDesc(any(Item.class))).thenReturn(listOfBooking);

        List<Booking> listOfBookingSaved = bookingService.getAllBookingByUser(item);

        assertThat(listOfBookingSaved.size(), equalTo(listOfBooking.size()));
        verify(bookingRepository, times(1))
                .findAllByItemOrderByStartDesc(item);
    }

    @Test
    void getAllBookingForItemByUser() {
        User owner = new User(1L, "Name", "user@mail.ru");
        Item item = new Item(1L, "Name", "About of item", owner, true);
        List<Booking> listOfBooking = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();

        when(bookingRepository.findByBookingByItemAndBookerAndEndBefore(any(Item.class), any(User.class),
                any(LocalDateTime.class))).thenReturn(listOfBooking);

        List<Booking> listOfBookingSaved = bookingService.getAllBookingForItemByUser(item, owner, time);

        assertThat(listOfBookingSaved.size(), equalTo(listOfBooking.size()));
        verify(bookingRepository, times(1))
                .findByBookingByItemAndBookerAndEndBefore(item, owner, time);
    }
}