package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @SneakyThrows
    @Test
    void createUser_whenUserIsCreate_thenResponseStatusOk() {
        UserDto userDto = new UserDto(1L, "Name", "user@mail.ru");
        when(userMapper.toUserDto(userService.createUser(any(UserDto.class)))).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserIsNotValid_thenResponseBadRequest() {
        UserDto userDto = new UserDto(1L, "Name", "mail.ru");
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is(400));

        verify(userService, never()).createUser(userDto);
    }

    @SneakyThrows
    @Test
    void getAllUsers_thenResponseStatusOk() {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getAllUsers();
    }

    @SneakyThrows
    @Test
    void getUserById_whenResponseStatusOk() {
        Long userId = 1L;
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @SneakyThrows
    @Test
    void getUserById_whenNotFound_thenResponseStatusBadRequest() {
        Long userId = 999L;

        when(userService.getUserById(userId))
                .thenThrow(NotFoundException.class);
        mockMvc.perform(get("/users/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userId))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserIsDone_thenResponseStatusOk() {
        UserDto userDto = new UserDto(1L, "NameUp", "userUpdate@email.ru");

        User user = new User(1L, "Name", "user@mail.ru");

        when(userMapper.toUserDto(userService.updateUser(any(Long.class), any(UserDto.class)))).thenReturn(userDto);

        String result = mockMvc.perform(patch("/users/{userId}", userDto.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void deleteUserBuId_thenResponseStatusOk() {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }
}