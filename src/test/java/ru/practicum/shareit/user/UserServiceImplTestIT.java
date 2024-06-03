package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class UserServiceImplTestIT {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void createUserTest() {
        User user = new User("Name", "user@mail.ru");
        User userSaved = userService.createUser(user);

        assertEquals(user.getId(), userSaved.getId());
        assertEquals(user.getUserName(), userSaved.getUserName());
        assertEquals(user.getEmail(), userSaved.getEmail());
    }

    @Test
    void updateUserTest() {
        User user = new User("Name", "user1@mail.ru");
        User userSaved = userService.createUser(user);
        Long userId = userSaved.getId();

        User newUser = new User("NewName", "user1@mail.ru");

        User updateUser = userService.updateUser(userId, newUser);

        assertEquals(user.getId(), updateUser.getId());
        assertEquals(newUser.getUserName(), updateUser.getUserName());
        assertEquals(newUser.getEmail(), updateUser.getEmail());
    }

    @Test
    void getAllUsers() {
        User user1 = new User("Name First", "user9@mail.ru");
        User user2 = new User("Name Second", "mail8@mail.ru");
        userService.createUser(user1);
        userService.createUser(user2);

        List<User> listOfUser = userService.getAllUsers();

        assertFalse(listOfUser.isEmpty());
    }

    @Test
    void getUserById() {
        User user = new User("Name", "user2@mail.ru");
        User userSaved = userService.createUser(user);
        Long userId = userSaved.getId();

        User savedUser = userService.getUserById(userId);

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getUserName(), savedUser.getUserName());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void deleteUser() {
        User user = new User("Name", "user5@mail.ru");
        User userSaved = userService.createUser(user);
        Long userId = userSaved.getId();

        HttpStatus httpStatus = userService.deleteUser(userId);
        assertEquals("200 OK", httpStatus.toString());
    }
}