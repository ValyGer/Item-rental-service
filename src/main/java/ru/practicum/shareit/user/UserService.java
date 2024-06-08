package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);

    User updateUser(Long userId, UserDto userDto);

    List<User> getAllUsers();

    User getUserById(Long userId);

    HttpStatus deleteUser(Long userId);
}
