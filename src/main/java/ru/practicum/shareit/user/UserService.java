package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Long userId, User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    void deleteUser(Long userId);
}
