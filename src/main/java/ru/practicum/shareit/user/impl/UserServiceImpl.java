package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // Создание нового пользователя
    public User createUser(User user) {
        try {
            User userCreate = userRepository.save(user);
            log.info("Пользователь {} успешно добавлен", user);
            return userCreate;
        } catch (ConflictException e) {
            log.error("Пользователь с такой почтой уже существует");
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }
    }

    // Обновление пользователя
    public User updateUser(Long userId, User user) {
        User saved = getUserById(userId);
        if (user.getUserName() != null) {
            saved.setUserName(user.getUserName());
        }
        if (user.getEmail() != null) {
            saved.setEmail(user.getEmail());
        }
        try {
            User userUpdate = userRepository.save(saved);
            log.info("Пользователь {} успешно обновлен", saved);
            return userUpdate;
        } catch (ConflictException e) {
            log.error("Пользователь с такой почтой уже существует");
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }
    }

    // Получение всех пользователей
    @Transactional
    public List<User> getAllUsers() {
        log.info("Получен список пользователей");
        return userRepository.findAll();
    }

    // Получение пользователя по Id
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Возвращен пользователь с id = {}", userId);
            return user.get();
        } else {
            log.info("Пользователь c id = {} не найден", userId);
            throw new NotFoundException("Пользователь с указанным id не найден");
        }
    }

    // Удаление пользователя
    public HttpStatus deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            log.info("Пользователь с id = {}, успешно удален", userId);
            return HttpStatus.OK;
        } catch (RuntimeException e) {
            log.info("Пользователь c id = {} не найден", userId);
            throw new RuntimeException("Пользователь с указанным id не найден");
        }
    }
}
