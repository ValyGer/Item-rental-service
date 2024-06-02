package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ItemRequestMapperTest {

//    @Test
//    void toItemRequestTest() {
//        ItemDtoForRequest itemDtoForRequest1 = new ItemDtoForRequest(1L, "Name1", "Description1",
//                true, 1L);
//        ItemDtoForRequest itemDtoForRequest2 = new ItemDtoForRequest(2L, "Name2", "Description2",
//                true, 2L);
//        List<ItemDtoForRequest> listOfRequest = new ArrayList<>();
//        listOfRequest.add(itemDtoForRequest1);
//        listOfRequest.add(itemDtoForRequest2);
//        User user = new User(1L, "Mail", "mail@mail.ru");
//
//        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description of Request", user.getId(),
//                LocalDateTime.now(), listOfRequest);
//
//        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
//
//
//        assertEquals(itemRequest.getId(), itemRequestDto.getId());
//        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
//        assertEquals(itemRequest.getRequester().getId(), itemRequestDto.getOwnerId());
//        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
//        assertEquals(itemRequest.getItems().size(), itemRequestDto.getItems().size());
//    }
//
//    @Test
//    void toItemRequestDtoTest() {
//
//
//
//
//    }

    @Test
    void getIdRequesterTest() {
        User user = new User(99L, "Mail", "mail@mail.ru");
        Long ownerId = user.getId();

        assertEquals(99L, ownerId);
    }
}



