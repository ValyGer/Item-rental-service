package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @SneakyThrows
    @Test
    void updateItemById_whenResponseStatusOk() {
        ItemDto itemDto = new ItemDto(1L, "Name", "About of item", true, 1L);
        User user = new User(1L, "Name", "user@mail.ru");

        when(itemMapper.toItemDto(itemService.updateItem(user.getId(), itemDto.getId(), any(Item.class)))).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemDto.getId())
                        .header("X-Sharer-User-Id", user.getId())
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
    void getAllItemsByUser_whenResponseStatusOk() {
        Long userId = 1L;

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(itemService).getAllItemsUser(userId);
    }

    @SneakyThrows
    @Test
    void getAllItemsByUser_whenResponseStatusBadRequest() {
        Long userId = 1L;

        when(itemService.getAllItemsUser(userId))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json"))
                .andExpect(status().is(404));
    }

    @SneakyThrows
    @Test
    void getItemWithBooker_whenResponseStatusOk() {
        Long itemId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(itemService).getItemWithBooker(itemId, userId);
    }

    @SneakyThrows
    @Test
    void searchAvailableItems() {
        String text = "ручка";
        Long itemId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/items/search")
                        .param("text", "ручка")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(itemService).searchAvailableItems(text);
    }

    @SneakyThrows
    @Test
    void addCommentToItem() {
        Comment comment = new Comment("Item is good");
        CommentDto commentDto = new CommentDto(1L, "Item is good", "Commentator", LocalDateTime.now());
        ItemDto itemDto = new ItemDto(1L, "Name", "About of item", true, 1L);
        User user = new User(1L, "Name", "user@mail.ru");

        when(commentMapper.toCommentDto(itemService.addComment(user.getId(), item.getItemId(), comment))).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemDto.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}