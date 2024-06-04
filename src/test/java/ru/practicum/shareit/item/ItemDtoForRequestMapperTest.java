package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemDtoForRequestMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemDtoForRequestMapperTest {

    @InjectMocks
    ItemDtoForRequestMapperImpl itemDtoForRequestMapper;

    @Test
    void toItemDtoForRequest() {
        LocalDateTime time = LocalDateTime.now();
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("text"));
        List<Booking> bookings = new ArrayList<>();
        User booker = new User(2L, "name", "email@mail.ru");
        Item item = new Item(1L, "telephone", "For call your friends", 1L, true, 2L);
        bookings.add(new Booking(1L, item, booker, time.plusMinutes(-20), time.plusMinutes(20)));

        ItemDtoForRequest itemDtoForRequest = itemDtoForRequestMapper.toItemDtoForRequest(item);

        assertEquals(item.getItemId(), itemDtoForRequest.getId());
        assertEquals(item.getIsAvailable(), itemDtoForRequest.getAvailable());
        assertEquals(item.getRequest(), itemDtoForRequest.getRequestId());
        assertEquals(item.getName(), itemDtoForRequest.getName());
        assertEquals(item.getDescription(), itemDtoForRequest.getDescription());
    }

    @Test
    void toItemDtoForRequestNull() {
        Item item = null;
        ItemDtoForRequest itemDtoForRequest = itemDtoForRequestMapper.toItemDtoForRequest(item);

        assertNull(itemDtoForRequest);
    }

    @Test
    void toItem() {
        LocalDateTime time = LocalDateTime.now();
        ItemDtoForRequest itemDtoForRequest = new ItemDtoForRequest(2L, "name",
                "String description", true, 1L);

        Item item = itemDtoForRequestMapper.toItem(itemDtoForRequest);

        assertEquals(itemDtoForRequest.getId(), item.getItemId());
        assertEquals(itemDtoForRequest.getAvailable(), item.getIsAvailable());
        assertEquals(itemDtoForRequest.getRequestId(), item.getRequest());
        assertEquals(itemDtoForRequest.getName(), item.getName());
        assertEquals(itemDtoForRequest.getDescription(), item.getDescription());
    }

    @Test
    void toItemNull() {
        ItemDtoForRequest itemDtoForRequest = null;
        Item item = itemDtoForRequestMapper.toItem(itemDtoForRequest);
        assertNull(item);
    }
}