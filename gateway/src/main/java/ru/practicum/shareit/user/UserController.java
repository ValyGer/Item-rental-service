package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.debug("Создан запрос на создание пользователя: {}", userDto);
        return userClient.createUser(userDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllUsers() {
        log.debug("Получение всех пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable @Positive long userId) {
        log.debug("Получить пользователя с id: {}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive long userId, @Valid @RequestBody UserDto userDto) {
        log.debug("Обновляется пользователь с id: {}", userId);
        userDto.setId(userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserBuId(@PathVariable @Positive long userId) {
        log.debug("Удаляется пользователь с id: {}", userId);
        return userClient.deleteUserBuId(userId);
    }
}
