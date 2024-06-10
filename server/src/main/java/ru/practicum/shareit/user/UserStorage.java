package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    HttpStatus deleteUser(Long userId);

    boolean isUserFound(Long userId);
}
