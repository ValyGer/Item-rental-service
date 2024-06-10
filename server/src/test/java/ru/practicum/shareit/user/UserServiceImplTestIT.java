package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTestIT {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void createUserTest() {
        UserDto userDto = new UserDto("Name", "user@mail.ru");
        User userSaved = userService.createUser(userDto);

        assertNotNull(userSaved.getId());
        assertEquals(userDto.getName(), userSaved.getUserName());
        assertEquals(userDto.getEmail(), userSaved.getEmail());
    }

    @Test
    void updateUserTest() {
        UserDto userDto = new UserDto("Name", "user@mail.ru");
        User userSaved = userService.createUser(userDto);
        Long userId = userSaved.getId();

        UserDto newUserDto = new UserDto("NewName", "user1@mail.ru");

        User updateUser = userService.updateUser(userId, newUserDto);

        assertEquals(userId, updateUser.getId());
        assertEquals(newUserDto.getName(), updateUser.getUserName());
        assertEquals(newUserDto.getEmail(), updateUser.getEmail());
    }

    @Test
    void getAllUsers() {
        UserDto userDto1 = new UserDto("Name First", "user9@mail.ru");
        UserDto userDto2 = new UserDto("Name Second", "mail8@mail.ru");
        userService.createUser(userDto1);
        userService.createUser(userDto2);

        List<User> listOfUser = userService.getAllUsers();

        assertFalse(listOfUser.isEmpty());
    }

    @Test
    void getUserById() {
        UserDto userDto = new UserDto("Name", "user2@mail.ru");
        User userSaved = userService.createUser(userDto);
        Long userId = userSaved.getId();

        User savedUser = userService.getUserById(userId);

        assertEquals(userId, savedUser.getId());
        assertEquals(userDto.getName(), savedUser.getUserName());
        assertEquals(userDto.getEmail(), savedUser.getEmail());
    }

    @Test
    void deleteUser() {
        UserDto userDto = new UserDto("Name", "user5@mail.ru");
        User userSaved = userService.createUser(userDto);
        Long userId = userSaved.getId();

        HttpStatus httpStatus = userService.deleteUser(userId);
        assertEquals("200 OK", httpStatus.toString());
    }
}