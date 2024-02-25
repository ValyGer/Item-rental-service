package ru.practicum.shareit.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MemoryUserStorageImpl implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private static long generateUserId = 0L;

    public User createUser(User user) {
        if (IsEmailBusy(user)) {
            user.setUserId(++generateUserId);
            users.put(user.getUserId(), user);
            log.info("Пользователь {} успешно добавлен", user);
            return user;
        } else {
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }
    }

    public User updateUser(User user) {
        User saved = users.get(user.getUserId());
        if (saved == null) {
            log.info("Пользователь c id = {} не найден", user.getUserId());
            throw new RuntimeException("Пользователь с указанным id не найден");
        } else if (user.getEmail() == null) {
            log.info("Имя пользователя {} обновлено", saved);
            saved.setUserName(user.getUserName());
            users.put(saved.getUserId(), saved);
            return saved;
        } else if (user.getUserName() == null) {
            if ((IsEmailBusy(user)) || (user.getEmail().equals(saved.getEmail())) ) {
                log.info("Почта пользователя {} успешно обновлена", saved);
                saved.setEmail(user.getEmail());
                users.put(saved.getUserId(), saved);
                return saved;
            } else {
                throw new ConflictException("Пользователь с такой почтой уже существует");
            }
        } else {
            log.info("Пользователь {} успешно обновлен", saved);
            users.put(saved.getUserId(), user);
            return user;
        }
    }

    public List<User> getAllUsers() {
        log.info("Возвращен весь список пользователей");
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            log.info("Возвращен пользователь с id = {}", userId);
            return users.get(userId);
        } else {
            log.info("Пользователь c id = {} не найден", userId);
            throw new RuntimeException("Пользователь с указанным id не найден");
        }
    }

    public HttpStatus deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            log.info("Возвращен пользователь с id = {}", userId);
            users.remove(userId);
            return HttpStatus.OK;
        } else {
            log.info("Пользователь c id = {} не найден", userId);
            throw new RuntimeException("Пользователь с указанным id не найден");
        }
    }

    private boolean IsEmailBusy (User user) {
        return  users.values()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList())
                .isEmpty();
    }
}
