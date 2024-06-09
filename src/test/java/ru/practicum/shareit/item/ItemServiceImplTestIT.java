package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
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
        UserDto userDto = new UserDto("User", "yandex@mail.ru");
        User savedUser = userService.createUser(userDto);
        ItemDto itemDto = new ItemDto("Name of Item", "Description of Item", true, null);

        Item savedItem = itemService.createItem(savedUser.getId(), itemDto);

        assertNotNull(savedItem.getItemId());
        assertEquals(itemDto.getName(), savedItem.getName());
        assertEquals(itemDto.getDescription(), savedItem.getDescription());
        assertEquals(itemDto.getAvailable(), savedItem.getIsAvailable());
        assertEquals(savedUser.getId(), savedItem.getOwner().getId());
    }

    @Test
    void updateItemTest() {
        UserDto userDto = new UserDto("User1", "yandex1@mail.ru");
        User savedUser = userService.createUser(userDto);
        ItemDto itemOldDto = new ItemDto("Name of Item1", "Description of Item1", true, null);
        Item savedItemOld = itemService.createItem(savedUser.getId(), itemOldDto);
        ItemDto itemNewDto = new ItemDto("Name of Item isNew", "Description of New Item", true, null);

        Item updateItem = itemService.updateItem(savedUser.getId(), savedItemOld.getItemId(), itemNewDto);

        assertEquals(savedItemOld.getItemId(), updateItem.getItemId());
        assertNotEquals(savedItemOld.getName(), updateItem.getName());
        assertNotEquals(savedItemOld.getDescription(), updateItem.getDescription());
        assertEquals(savedItemOld.getIsAvailable(), updateItem.getIsAvailable());
        assertEquals(savedItemOld.getOwner().getId(), updateItem.getOwner().getId());
    }

    @Test
    void getAllItemsUserTest() {
        UserDto userDto = new UserDto("User2", "yandex2@mail.ru");
        User savedUser = userService.createUser(userDto);
        ItemDto itemDto1 = new ItemDto("Name of Item11", "Description of Item11", true,
                null);
        Item savedItem1 = itemService.createItem(savedUser.getId(), itemDto1);
        ItemDto itemDto2 = new ItemDto("Name device12", "Description of device12", true, null);
        Item savedItem2 = itemService.createItem(savedUser.getId(), itemDto2);

        List<ItemDtoForBookingAndComments> allItemsOfUser = itemService.getAllItemsUser(savedUser.getId());
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
        UserDto userDto = new UserDto("User3", "yandex3@mail.ru");
        User savedUser = userService.createUser(userDto);
        ItemDto itemDto = new ItemDto("Name of Item23", "Description of Item23", true,
                null);
        Item savedItem = itemService.createItem(savedUser.getId(), itemDto);

        Item itemFromBd = itemService.getItemsById(savedItem.getItemId());

        assertEquals(itemFromBd.getItemId(), savedItem.getItemId());
        assertEquals(itemFromBd.getName(), savedItem.getName());
        assertEquals(itemFromBd.getDescription(), savedItem.getDescription());
        assertEquals(itemFromBd.getIsAvailable(), savedItem.getIsAvailable());
        assertEquals(itemFromBd.getOwner().getId(), savedItem.getOwner().getId());
    }

    @Test
    void searchAvailableItemsTest() {
        UserDto userDto = new UserDto("User10", "yandex10@mail.ru");
        User savedUser = userService.createUser(userDto);
        Item item1 = new Item("FirstItem in List", "Description of FirstItem", savedUser, true);
        ItemDto itemDto1 = new ItemDto("FirstItem in List", "Description of FirstItem", true,
                null);
        Item savedItem1 = itemService.createItem(savedUser.getId(), itemDto1);
        ItemDto itemDto2 = new ItemDto("SecondItem", "Description of SecondItem of list", true,
                null);
        Item savedItem2 = itemService.createItem(savedUser.getId(), itemDto2);

        List<Item> resultList = itemService.searchAvailableItems("list");

        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
    }

    @Test
    void addCommentTest() {
        LocalDateTime time = LocalDateTime.now();
        UserDto userDto = new UserDto("User20", "yandex20@mail.ru");
        User savedUser = userService.createUser(userDto); // владелец
        ItemDto itemDto = new ItemDto("Item111", "Description of Item", true, null);
        Item savedItem = itemService.createItem(savedUser.getId(), itemDto); // вещь
        savedItem.setOwner(savedUser);
        UserDto bookerDto = new UserDto("Booker", "booker@mail.ru");
        User savedBooker = userService.createUser(bookerDto); // арендатор
        BookingDto bookingDto = new BookingDto(savedItem.getItemId(), savedBooker.getId(), time.plusMinutes(-125L), time.plusMinutes(-50L),
                Status.APPROVED); // создаем бронирование
        bookingService.createBooking(savedBooker.getId(), bookingDto);
        CommentDto commentDto = new CommentDto("This is first comment");

        Comment savedComment = itemService.addComment(savedBooker.getId(), savedItem.getItemId(), commentDto);

        assertEquals(savedComment.getText(), commentDto.getText());
    }
}