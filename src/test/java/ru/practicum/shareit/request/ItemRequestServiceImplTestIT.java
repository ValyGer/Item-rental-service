package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class ItemRequestServiceImplTestIT {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserServiceImpl userService;

    @Test
    void createItemRequestTest() {
        User user = new User("Name74", "user74@mail.ru");
        userService.createUser(user);
        ItemRequest itemRequest = new ItemRequest("ItemRequest", user, LocalDateTime.now());

        ItemRequest savedItemRequest = itemRequestService.createItemRequest(itemRequest, user.getId());

        assertEquals(itemRequest.getId(), savedItemRequest.getId());
        assertEquals(itemRequest.getDescription(), savedItemRequest.getDescription());
        assertEquals(itemRequest.getCreated(), savedItemRequest.getCreated());
        assertEquals(itemRequest.getRequester().getUserName(), savedItemRequest.getRequester().getUserName());
    }

    @Test
    void getAllItemRequestOfUserTest() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User("Name77", "user77@mail.ru");
        userService.createUser(user);
        ItemRequest itemRequest1 = new ItemRequest("ItemRequest1", user, time.plusMinutes(-10));
        itemRequestService.createItemRequest(itemRequest1, user.getId());
        ItemRequest itemRequest2 = new ItemRequest("ItemRequest2", user, time.plusMinutes(2));
        itemRequestService.createItemRequest(itemRequest2, user.getId());

        List<ItemRequest> savedListItemOfRequest = itemRequestService
                .getAllItemRequestOfUser(user.getId(), 0, 20);

        assertEquals(2, savedListItemOfRequest.size());
        assertEquals(itemRequest1.getId(), savedListItemOfRequest.get(0).getId());
        assertEquals(itemRequest1.getDescription(), savedListItemOfRequest.get(0).getDescription());
        assertEquals(itemRequest1.getRequester().getUserName(), savedListItemOfRequest.get(0).getRequester().getUserName());

        assertEquals(itemRequest2.getId(), savedListItemOfRequest.get(1).getId());
        assertEquals(itemRequest2.getDescription(), savedListItemOfRequest.get(1).getDescription());
        assertEquals(itemRequest2.getRequester().getUserName(), savedListItemOfRequest.get(1).getRequester().getUserName());
    }

    @Test
    void getAllItemRequestOfOtherUsersTest() {
        LocalDateTime time = LocalDateTime.now();
        User user1 = new User("Name8", "user8@mail.ru");
        userService.createUser(user1);
        User user2 = new User("Name21", "user21@mail.ru");
        userService.createUser(user2);

        ItemRequest itemRequest1 = new ItemRequest("ItemRequest6", user1, time.plusMinutes(-10));
        itemRequestService.createItemRequest(itemRequest1, user1.getId());
        ItemRequest itemRequest2 = new ItemRequest("ItemRequest7", user2, time.plusMinutes(2));
        itemRequestService.createItemRequest(itemRequest2, user2.getId());

        User user3 = new User("Name10", "user10@mail.ru");
        userService.createUser(user3);

        List<ItemRequest> savedListItemOfOtherUsers = itemRequestService
                .getAllItemRequestOfOtherUsers(user3.getId(), 0, 20);

        assertEquals(6, savedListItemOfOtherUsers.size());
    }

    @Test
    void getItemRequestTest() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User("Name4", "user4@mail.ru");
        User savedUser = userService.createUser(user);
        ItemRequest itemRequest = new ItemRequest("ItemRequest7", user, time.plusMinutes(-10));
        ItemRequest savedItemRequest = itemRequestService.createItemRequest(itemRequest, user.getId());

        ItemRequest itemRequestReceivedFromRequest = itemRequestService.getItemRequest(savedItemRequest.getId(), savedUser.getId());

        assertEquals(itemRequest.getId(), itemRequestReceivedFromRequest.getId());
        assertEquals(itemRequest.getDescription(), itemRequestReceivedFromRequest.getDescription());
        assertEquals(itemRequest.getRequester().getUserName(), itemRequestReceivedFromRequest.getRequester().getUserName());
    }
}