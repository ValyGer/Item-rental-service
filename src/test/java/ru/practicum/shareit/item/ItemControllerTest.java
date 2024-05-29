package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private CommentMapper commentMapper;

    private Item item = new Item(1L, "Name", "About of item", 1L, true);

    @SneakyThrows
    @Test
    void createItem_whenItemIsCreate_thenResponseStatusOk() {
        ItemDto itemDto = new ItemDto(1L, "Name", "About of item", true, 1L);
        UserDto userDto = new UserDto(1L, "Name", "name@mail.ru");

        when(itemMapper.toItemDto(itemService.createItem(userDto.getId(), item)))
                .thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemIsNotCreate_thenResponseBadRequest() {
        ItemDto itemDto = new ItemDto(1L, null, "About of item", true, 1L);
        UserDto userDto = new UserDto(1L, "Name", "name@mail.ru");

        when(itemMapper.toItemDto(itemService.createItem(userDto.getId(), item)))
                .thenThrow(ValidationException.class);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().is(400));

        verify(itemService, never()).createItem(userDto.getId(), itemMapper.toItem(itemDto));
    }



    @Test
    void updateItem() {
    }

    @Test
    void getAllItemsUser() {
    }

    @Test
    void getItemWithBooker() {
    }

    @Test
    void searchAvailableItems() {
    }

    @Test
    void addCommentToItem() {
    }
}