package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // Создание нового пользователя
    public User createUser(User user) {
        log.debug("Вызван метод создания пользователя");
        return userRepository.save(user);
    }

    // Обновление пользователя
    public User updateUser(Long userId, User user) {
        log.debug("Вызван метод обновления пользователя");
        user.setUserId(userId);
        return userRepository.save(user);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        log.debug("Вызван метод получения всех пользователей");
        return userRepository.findAll();
    }

    // Получение пользователя по Id
    public User getUserById(Long userId) {
        log.debug("Вызван метод получения пользователя по Id");
        return userRepository.getReferenceById(userId);
    }

    // Удаление пользователя
    public void deleteUser(Long userId) {
        log.debug("Вызван метод удаления пользователя");
        userRepository.deleteById(userId);
    }

}
