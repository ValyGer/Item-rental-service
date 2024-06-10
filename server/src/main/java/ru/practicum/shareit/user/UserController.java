package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping // Добавление нового пользователя
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userMapper.toUserDto(userService.createUser(userDto)));
    }

    @GetMapping // Получение списка всех пользователей
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{userId}") // Получение пользователя по Id
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        return ResponseEntity.ok().body(userMapper.toUserDto(userService.getUserById(userId)));
    }

    @PatchMapping("/{userId}") // Обновление информации о пользователе
    public ResponseEntity<UserDto> updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userMapper.toUserDto(userService.updateUser(userId, userDto)));
    }

    @DeleteMapping("/{userId}") // Удаление пользователя по Id
    public ResponseEntity<HttpStatus> deleteUserBuId(@PathVariable long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}
