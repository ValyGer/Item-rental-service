package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    HttpStatus deleteUser(Long userId);

    boolean IsUserFound (Long userId);
}
