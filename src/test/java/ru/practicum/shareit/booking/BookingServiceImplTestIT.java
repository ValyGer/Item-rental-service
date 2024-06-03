package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.awt.print.Book;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingServiceImplTestIT {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    void createBooking() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName1","firstMail1@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName2","firstMail2@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device", "description of device", savedUser1.getId(),true,
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
    void setApprovedByOwner() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("FirstName3","firstMail3@mail.ru");
        User savedUser1 = userService.createUser(user1); // создаем владельца вещи

        User user2 = new User("FirstName4","firstMail4@mail.ru");
        User savedUser2 = userService.createUser(user2); // создаем арендатора вещи

        Item item1 = new Item("device2", "description of device2", savedUser1.getId(),true,
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


    //----Отсюда!
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
    void testGetAllBookingByUser() {
    }

    @Test
    void getAllBookingForItemByUser() {
    }
}