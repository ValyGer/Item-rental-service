package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private ItemRequestMapper itemRequestMapper;

    User user = new User(1L, "Name", "user@mail.ru");

    @SneakyThrows
    @Test
    void createItemRequest_whenItemRequestIsCreate_thenResponseStatusOk() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "ItemRequest", 1L, LocalDateTime.now());

        when(itemRequestMapper
                .toItemRequestDto(itemRequestService.createItemRequest(itemRequestDto, user.getId())))
                .thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void getAllItemRequestOfUser_whenResponseStatusOk() {
        Integer from = 0;
        Integer size = 10;

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(itemRequestService).getAllItemRequestOfUser(user.getId(), from, size);
    }

    @SneakyThrows
    @Test
    void getAllItemRequestOfOtherUsers_whenResponseStatusOk() {
        Integer from = 0;
        Integer size = 10;

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(itemRequestService).getAllItemRequestOfOtherUsers(user.getId(), from, size);
    }

    @SneakyThrows
    @Test
    void getItemRequestById_whenResponseStatusOk() {
        Long requestId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", user.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(itemRequestService).getItemRequest(requestId, user.getId());
    }
}