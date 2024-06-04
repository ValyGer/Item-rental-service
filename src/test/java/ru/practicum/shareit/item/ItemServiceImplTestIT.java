package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndComments;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndCommentsMapper;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplTestIT {
    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemDtoForBookingAndCommentsMapper itemDtoForBookingAndCommentsMapper;
    @Autowired
    private BookingLastNextDtoMapper bookingLastNextDtoMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void createItemTest() {
        User user = new User("User", "yandex@mail.ru");
        User savedUser = userService.createUser(user);
        Item item = new Item("Name of Item", "Description of Item", savedUser.getId(), true, null);

        Item savedItem = itemService.createItem(savedUser.getId(), item);

        assertEquals(item.getItemId(), savedItem.getItemId());
        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getDescription(), savedItem.getDescription());
        assertEquals(item.getIsAvailable(), savedItem.getIsAvailable());
        assertEquals(item.getOwner(), savedItem.getOwner());
    }

    @Test
    void updateItemTest() {
        User user = new User("User1", "yandex1@mail.ru");
        User savedUser = userService.createUser(user);
        Item itemOld = new Item("Name of Item1", "Description of Item1", savedUser.getId(), true,
                null);
        Item savedItemOld = itemService.createItem(savedUser.getId(), itemOld);
        Item itemNew = new Item("Name of Item isNew", "Description of New Item", savedUser.getId(),
                true, null);

        Item updateItem = itemService.updateItem(savedUser.getId(), itemOld.getItemId(), itemNew);

        assertEquals(savedItemOld.getItemId(), updateItem.getItemId());
        assertNotEquals(savedItemOld.getName(), updateItem.getName());
        assertNotEquals(savedItemOld.getDescription(), updateItem.getDescription());
        assertEquals(savedItemOld.getIsAvailable(), updateItem.getIsAvailable());
        assertEquals(savedItemOld.getOwner(), updateItem.getOwner());
    }

    @Test
    void getAllItemsUserTest() {
        User user = new User("User2", "yandex2@mail.ru");
        User savedUser = userService.createUser(user);
        Item item1 = new Item("Name of Item11", "Description of Item11", savedUser.getId(), true,
                null);
        Item savedItem1 = itemService.createItem(savedUser.getId(), item1);
        Item item2 = new Item("Name device12", "Description of device12", savedUser.getId(),
                true, null);
        Item savedItem2 = itemService.createItem(savedUser.getId(), item2);

        List<ItemDtoForBookingAndComments> allItemsOfUser = itemService.getAllItemsUser(user.getId());
        System.out.println(allItemsOfUser);
        assertFalse(allItemsOfUser.isEmpty());
        assertEquals(2, allItemsOfUser.size());
        assertEquals(allItemsOfUser.get(0).getId(), savedItem1.getItemId());
        assertEquals(allItemsOfUser.get(0).getName(), savedItem1.getName());
        assertEquals(allItemsOfUser.get(0).getDescription(), savedItem1.getDescription());
        assertEquals(allItemsOfUser.get(0).getAvailable(), savedItem1.getIsAvailable());

        assertEquals(allItemsOfUser.get(1).getId(), savedItem2.getItemId());
        assertEquals(allItemsOfUser.get(1).getName(), savedItem2.getName());
        assertEquals(allItemsOfUser.get(1).getDescription(), savedItem2.getDescription());
        assertEquals(allItemsOfUser.get(1).getAvailable(), savedItem2.getIsAvailable());
    }

    @Test
    void getItemsByIdTest() {
        User user = new User("User3", "yandex3@mail.ru");
        User savedUser = userService.createUser(user);
        Item item = new Item("Name of Item23", "Description of Item23", savedUser.getId(), true,
                null);
        Item savedItem = itemService.createItem(savedUser.getId(), item);

        Item itemFromBd = itemService.getItemsById(savedItem.getItemId());

        assertEquals(itemFromBd.getItemId(), savedItem.getItemId());
        assertEquals(itemFromBd.getName(), savedItem.getName());
        assertEquals(itemFromBd.getDescription(), savedItem.getDescription());
        assertEquals(itemFromBd.getIsAvailable(), savedItem.getIsAvailable());
        assertEquals(itemFromBd.getOwner(), savedItem.getOwner());
    }

    @Test
    void searchAvailableItemsTest() {
        User user = new User("User10", "yandex10@mail.ru");
        User savedUser = userService.createUser(user);
        Item item1 = new Item("FirstItem in List", "Description of FirstItem", savedUser.getId(), true,
                null);
        Item savedItem1 = itemService.createItem(savedUser.getId(), item1);
        Item item2 = new Item("SecondItem", "Description of SecondItem of list", savedUser.getId(), true,
                null);
        Item savedItem2 = itemService.createItem(savedUser.getId(), item2);

        List<Item> resultList = itemService.searchAvailableItems("list");

        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
    }

    @Test
    void addCommentTest() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User("User20", "yandex20@mail.ru");
        User savedUser = userService.createUser(user); // владелец
        Item item = new Item("Item111", "Description of Item", savedUser.getId(), true,
                null);
        Item savedItem = itemService.createItem(savedUser.getId(), item); // вещь
        User booker = new User("Booker", "booker@mail.ru");
        User savedBooker = userService.createUser(booker); // арендатор
        Booking booking = new Booking(savedItem, savedBooker, time.plusMinutes(-125L), time.plusMinutes(-50L),
                Status.APPROVED); // создаем бронирование
        bookingService.createBooking(booking);
        Comment comment = new Comment("This is first comment");

        Comment savedComment = itemService.addComment(savedBooker.getId(), savedItem.getItemId(), comment);

        assertEquals(savedComment, comment);
    }
}