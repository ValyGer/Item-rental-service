package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class BookingServiceImplTestIT {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    void createBookingTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName1", "firstMail1@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName2", "firstMail2@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device", "description of device", savedUser1.getId(), true,
                null);  // создаем вещь
        Item savedItem1 = itemService.createItem(savedUser1.getId(), item1);

        Booking booking1 = new Booking(savedItem1, savedUser2, time.plusMinutes(-25L), time.plusMinutes(50L),
                Status.WAITING); // создаем бронирование
        Booking savedBooking1 = bookingService.createBooking(booking1);

        assertEquals(booking1.getBooker().getId(), savedBooking1.getBooker().getId());
        assertEquals(booking1.getItem().getItemId(), savedBooking1.getItem().getItemId());
        assertEquals(booking1.getStatus(), savedBooking1.getStatus());
    }

    @Test
    void setApprovedByOwnerTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName3", "firstMail3@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName4", "firstMail4@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device2", "description of device2", savedUser1.getId(), true,
                null);  // создаем вещь
        Item savedItem1 = itemService.createItem(savedUser1.getId(), item1);

        Booking booking1 = new Booking(savedItem1, savedUser2, time.plusMinutes(-25L), time.plusMinutes(50L),
                Status.WAITING); // создаем бронирование
        Booking savedBooking1 = bookingService.createBooking(booking1);

        savedBooking1.setStatus(Status.APPROVED);

        Booking updateBooking = bookingService.setApprovedByOwner(user1.getId(), savedBooking1.getBookingId(), true);

        assertEquals(savedBooking1.getBookingId(), updateBooking.getBookingId());
        assertEquals(savedBooking1.getBooker().getId(), updateBooking.getBooker().getId());
        assertEquals(savedBooking1.getItem().getItemId(), updateBooking.getItem().getItemId());
        assertEquals(Status.APPROVED, updateBooking.getStatus());
    }

    @Test
    void getBookingByIdTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName5", "firstMail5@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName6", "firstMail6@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи
        Long userId2 = user2.getId();

        Item item1 = new Item("device3", "description of device3", savedUser1.getId(), true,
                null);  // создаем вещь
        Item savedItem1 = itemService.createItem(savedUser1.getId(), item1);

        Booking booking1 = new Booking(savedItem1, savedUser2, time.plusMinutes(-25L), time.plusMinutes(50L),
                Status.WAITING); // создаем бронирование
        Booking savedBooking1 = bookingService.createBooking(booking1);
        Long bookingId = savedBooking1.getBookingId();

        Booking receivedBooking = bookingService.getBookingById(userId2, bookingId);

        assertEquals(savedBooking1.getBookingId(), receivedBooking.getBookingId());
        assertEquals(savedBooking1.getBooker().getId(), receivedBooking.getBooker().getId());
        assertEquals(savedBooking1.getItem().getItemId(), receivedBooking.getItem().getItemId());
        assertEquals(Status.WAITING, receivedBooking.getStatus());
    }

    @Test
    void getAllBookingByUserTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName7", "firstMail7@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName8", "firstMail8@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device4", "description of device4", savedUser1.getId(), true,
                null);  // создаем 1 вещь
        Item savedItem1 = itemService.createItem(savedUser1.getId(), item1);

        Item item2 = new Item("device5", "description of device5", savedUser1.getId(), true,
                null);  // создаем 2 вещь
        Item savedItem2 = itemService.createItem(savedUser1.getId(), item2);

        Booking booking1 = new Booking(savedItem1, savedUser2, time.plusMinutes(-25L), time.plusMinutes(50L),
                Status.WAITING); // создаем бронирование 1
        bookingService.createBooking(booking1);

        Booking booking2 = new Booking(savedItem2, savedUser2, time.plusMinutes(-10L), time.plusMinutes(40L),
                Status.WAITING); // создаем бронирование 2
        bookingService.createBooking(booking2);

        Booking booking3 = new Booking(savedItem1, savedUser2, time.plusMinutes(55L), time.plusMinutes(150L),
                Status.WAITING); // создаем бронирование 3
        bookingService.createBooking(booking3);

        List<BookingDtoWithItem> listBookingOfUser = bookingService.getAllBookingByUser(0, 20, savedUser2.getId(), "ALL");

        assertFalse(listBookingOfUser.isEmpty());
        assertEquals(listBookingOfUser.size(), 3);
    }

    @Test
    void getAllBookingByOwnerTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("1Name", "firstMail11@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("2Name", "firstMail13@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device7", "description of device7", savedUser1.getId(), true,
                null);  // создаем 1 вещь
        Item savedItem1 = itemService.createItem(savedUser1.getId(), item1);

        Item item2 = new Item("device8", "description of device8", savedUser1.getId(), true,
                null);  // создаем 2 вещь
        Item savedItem2 = itemService.createItem(savedUser1.getId(), item2);

        Booking booking1 = new Booking(savedItem1, savedUser2, time.plusMinutes(-25L), time.plusMinutes(50L),
                Status.WAITING); // создаем бронирование 1
        bookingService.createBooking(booking1);

        Booking booking2 = new Booking(savedItem2, savedUser2, time.plusMinutes(-10L), time.plusMinutes(40L),
                Status.WAITING); // создаем бронирование 2
        bookingService.createBooking(booking2);

        Booking booking3 = new Booking(savedItem1, savedUser2, time.plusMinutes(55L), time.plusMinutes(150L),
                Status.WAITING); // создаем бронирование 3
        bookingService.createBooking(booking3);

        List<Booking> listBookingOfOwner = bookingService.getAllBookingByOwner(0, 20, savedUser1.getId(), "ALL");

        assertFalse(listBookingOfOwner.isEmpty());
        assertEquals(listBookingOfOwner.size(), 3);
    }

    @Test
    void getAllBookingForItemByUserTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName9", "firstMail9@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName10", "firstMail10@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device5", "description of device5", savedUser1.getId(), true,
                null);  // создаем 1 вещь
        Item savedItem1 = itemService.createItem(savedUser1.getId(), item1);

        Item item2 = new Item("device6", "description of device6", savedUser1.getId(), true,
                null);  // создаем 2 вещь
        Item savedItem2 = itemService.createItem(savedUser1.getId(), item2);

        Booking booking1 = new Booking(savedItem1, savedUser2, time.plusMinutes(-25L), time.plusMinutes(50L),
                Status.WAITING); // создаем бронирование 1
        bookingService.createBooking(booking1);

        Booking booking2 = new Booking(savedItem2, savedUser2, time.plusMinutes(-10L), time.plusMinutes(40L),
                Status.WAITING); // создаем бронирование 2
        bookingService.createBooking(booking2);

        Booking booking3 = new Booking(savedItem1, savedUser2, time.plusMinutes(55L), time.plusMinutes(150L),
                Status.WAITING); // создаем бронирование 3
        bookingService.createBooking(booking3);

        List<Booking> listBookingOfItemForUser = bookingService
                .getAllBookingForItemByUser(savedItem1, savedUser2, time.plusMinutes(60L));
        assertFalse(listBookingOfItemForUser.isEmpty());
        assertEquals(listBookingOfItemForUser.size(), 1);
    }
}