package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemDtoForRequestMapper;
import ru.practicum.shareit.item.dto.ItemDtoForRequestMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @InjectMocks
    ItemRequestMapperImpl itemRequestMapper;

    @Mock
    ItemDtoForRequestMapperImpl itemDtoForRequestMapper = new ItemDtoForRequestMapperImpl();

    @Test
    void toItemRequestTest() {
        ItemDtoForRequest itemDtoForRequest1 = new ItemDtoForRequest(1L, "Name1", "Description1",
                true, 1L);
        ItemDtoForRequest itemDtoForRequest2 = new ItemDtoForRequest(2L, "Name2", "Description2",
                true, 2L);

        List<ItemDtoForRequest> listOfRequest = new ArrayList<>();
        listOfRequest.add(itemDtoForRequest1);
        listOfRequest.add(itemDtoForRequest2);
        User user = new User(1L, "Mail", "mail@mail.ru");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description of Request", user.getId(),
                LocalDateTime.now(), listOfRequest);

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
        assertEquals(itemRequest.getItems().size(), itemRequestDto.getItems().size());
    }

    @Test
    void toItemRequestDtoTest() {
        User user = new User(1L, "Mail", "mail@mail.ru");
        LocalDateTime time = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest(1L, "Description", user, time);

        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequestDto.getId(), itemRequest.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDto.getOwnerId(), itemRequest.getRequester().getId());
        assertEquals(itemRequestDto.getCreated(), itemRequest.getCreated());
    }

    @Test
    void getIdRequesterTest() {
        User user = new User(99L, "Mail", "mail@mail.ru");
        Long ownerId = user.getId();

        assertEquals(99L, ownerId);
    }
}



