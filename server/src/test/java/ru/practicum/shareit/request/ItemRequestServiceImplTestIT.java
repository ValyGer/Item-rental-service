package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
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
        UserDto userDto = new UserDto("Name74", "user74@mail.ru");
        User user = userService.createUser(userDto);
        ItemRequestDto itemRequestDto = new ItemRequestDto("ItemRequest", user.getId(), LocalDateTime.now());

        ItemRequest savedItemRequest = itemRequestService.createItemRequest(itemRequestDto, user.getId());

        assertEquals(itemRequestDto.getDescription(), savedItemRequest.getDescription());
        assertEquals(user.getUserName(), savedItemRequest.getRequester().getUserName());
    }

    @Test
    void getAllItemRequestOfUserTest() {
        LocalDateTime time = LocalDateTime.now();
        UserDto userDto = new UserDto("Name77", "user77@mail.ru");
        User user = userService.createUser(userDto);
        ItemRequestDto itemRequestDto1 = new ItemRequestDto("ItemRequest1", user.getId(), time.plusMinutes(-10));
        itemRequestService.createItemRequest(itemRequestDto1, user.getId());
        ItemRequestDto itemRequestDto2 = new ItemRequestDto("ItemRequest2", user.getId(), time.plusMinutes(2));
        itemRequestService.createItemRequest(itemRequestDto2, user.getId());

        List<ItemRequest> savedListItemOfRequest = itemRequestService
                .getAllItemRequestOfUser(user.getId(), 0, 20);

        assertEquals(2, savedListItemOfRequest.size());
        assertEquals(itemRequestDto1.getDescription(), savedListItemOfRequest.get(0).getDescription());
        assertEquals(user.getUserName(), savedListItemOfRequest.get(0).getRequester().getUserName());

        assertEquals(itemRequestDto2.getDescription(), savedListItemOfRequest.get(1).getDescription());
        assertEquals(user.getUserName(), savedListItemOfRequest.get(1).getRequester().getUserName());
    }

    @Test
    void getAllItemRequestOfOtherUsersTest() {
        LocalDateTime time = LocalDateTime.now();
        UserDto userDto1 = new UserDto("Name8", "user8@mail.ru");
        User user1 = userService.createUser(userDto1);
        UserDto userDto2 = new UserDto("Name21", "user21@mail.ru");
        User user2 = userService.createUser(userDto2);

        ItemRequestDto itemRequestDto1 = new ItemRequestDto("ItemRequest6", user1.getId(), time.plusMinutes(-10));
        itemRequestService.createItemRequest(itemRequestDto1, user1.getId());
        ItemRequestDto itemRequestDto2 = new ItemRequestDto("ItemRequest7", user2.getId(), time.plusMinutes(2));
        itemRequestService.createItemRequest(itemRequestDto2, user2.getId());

        UserDto userDto3 = new UserDto("Name10", "user10@mail.ru");
        User user3 = userService.createUser(userDto3);

        List<ItemRequest> savedListItemOfOtherUsers = itemRequestService
                .getAllItemRequestOfOtherUsers(user3.getId(), 0, 20);

        assertEquals(6, savedListItemOfOtherUsers.size());
    }

    @Test
    void getItemRequestTest() {
        LocalDateTime time = LocalDateTime.now();
        UserDto userDto = new UserDto("Name4", "user4@mail.ru");
        User savedUser = userService.createUser(userDto);
        ItemRequestDto itemRequestDto = new ItemRequestDto("ItemRequest7", savedUser.getId(), time.plusMinutes(-10));
        ItemRequest savedItemRequest = itemRequestService.createItemRequest(itemRequestDto, savedUser.getId());

        ItemRequest itemRequestReceivedFromRequest = itemRequestService.getItemRequest(savedItemRequest.getId(), savedUser.getId());

        assertEquals(savedItemRequest.getId(), itemRequestReceivedFromRequest.getId());
        assertEquals(savedItemRequest.getDescription(), itemRequestReceivedFromRequest.getDescription());
        assertEquals(savedItemRequest.getRequester().getUserName(), itemRequestReceivedFromRequest.getRequester().getUserName());
    }
}