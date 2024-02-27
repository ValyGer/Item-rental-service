package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    // Создание нового пользователя
    public User createUser(User user) {
        log.debug("Вызван метод создания пользователя");
        return userStorage.createUser(user);
    }

    // Обновление пользователя
    public User updateUser(User user) {
        log.debug("Вызван метод обновления пользователя");
        return userStorage.updateUser(user);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        log.debug("Вызван метод получения всех пользователей");
        return userStorage.getAllUsers();
    }

    // Получение пользователя по Id
    public User getUserById(Long userId) {
        log.debug("Вызван метод получения пользователя по Id");
        return userStorage.getUserById(userId);
    }

    // Удаление пользователя
    public HttpStatus deleteUser(Long userId) {
        log.debug("Вызван метод удаления пользователя");
        return userStorage.deleteUser(userId);
    }

}
