package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@Transactional
class BookingRepositoryTest {

    Long bookingId;
    Long userId;
    Long userId1;
    Long itemId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private LocalDateTime time = LocalDateTime.now();

    @BeforeEach
    private void addSourceData() {
        User user1L = userRepository.save(new User(
                "Name",
                "name@mail.ru"
        ));

        User user2L = userRepository.save(new User(
                "Secondary",
                "mail@mail.ru"
        ));
        userId = user2L.getId();

        User user3L = userRepository.save(new User(
                "User",
                "user@mail.ru"
        ));
        userId1 = user3L.getId();

        Item item1L = itemRepository.save(new Item(
                "Telephone",
                "A device for calls and correspondence with your friends",
                user1L.getId(),
                true,
                null
        ));

        Item item2L = itemRepository.save(new Item(
                "Camera",
                "Allows you to take great photos",
                user2L.getId(),
                true,
                null
        ));
        itemId = item2L.getItemId();

        Item item3L = itemRepository.save(new Item(
                "Curtains",
                "They will hide you from the sun",
                user1L.getId(),
                false,
                null
        ));

        Booking booking1 = bookingRepository.save(new Booking(
                item1L,
                user3L,
                time.plusMinutes(-25L),
                time.plusMinutes(50L),
                Status.WAITING
        ));

        Booking booking2 = bookingRepository.save(new Booking(
                item2L,
                user3L,
                time.plusMinutes(15L),
                time.plusMinutes(40L),
                Status.APPROVED
        ));

        Booking booking3 = bookingRepository.save(new Booking(
                item2L,
                user2L,
                time.plusMinutes(-10L),
                time.plusMinutes(10L),
                Status.WAITING
        ));

        Booking booking4 = bookingRepository.save(new Booking(
                item2L,
                user3L,
                time.plusMinutes(-90L),
                time.plusMinutes(-25L),
                Status.REJECTED
        ));
        bookingId = booking4.getBookingId();
    }

    @Test
    void findAllBookingsForBookerWithStartAndEndTest() {
        User user = userRepository.getReferenceById(userId1);
        LocalDateTime start = time.plusMinutes(-10L);
        LocalDateTime end = time.plusMinutes(10L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> listOfBooking = bookingRepository.findAllBookingsForBookerWithStartAndEnd(user, start, end, pageRequest);

        assertEquals(1, listOfBooking.size());
    }

    @Test
    void findAllByItem_OwnerAndEndIsBeforeOrderByStartDescTest() {
        LocalDateTime now = time.plusMinutes(5L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> listOfBooking = bookingRepository
                .findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(userId, now, pageRequest);

        assertFalse(listOfBooking.isEmpty());
        assertEquals(1, listOfBooking.size());
    }

    @Test
    void findAllByItemOrderByStartDescTest() {
        Optional<Item> item = itemRepository.findById(itemId);
        List<Booking> listOfBooking = bookingRepository.findAllByItemOrderByStartDesc(item.get());

        assertFalse(listOfBooking.isEmpty());
        assertEquals(3, listOfBooking.size());
        assertEquals("Camera", listOfBooking.get(0).getItem().getName());
    }

    @Test
    void findByBookingByItemAndBookerAndEndBeforeTest() {
        Optional<Item> item = itemRepository.findById(itemId);
        Optional<User> user = userRepository.findById(userId1);
        List<Booking> listOfBooking = bookingRepository
                .findByBookingByItemAndBookerAndEndBefore(item.get(), user.get(), time.plusMinutes(-20L));

        assertFalse(listOfBooking.isEmpty());
        assertEquals(1, listOfBooking.size());
    }

    @AfterEach
    private void deleteAllRepository() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

}