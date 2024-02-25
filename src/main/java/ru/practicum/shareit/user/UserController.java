package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping // Добавление нового пользователя
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.createUser(user));
    }

    @GetMapping // Получение списка всех пользователей
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}") // Получение пользователя по Id
    public UserDto getUserById(@PathVariable long userId) {
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @PatchMapping("/{userId}") // Обновление информации о пользователе
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userId, userDto);
        return UserMapper.toUserDto(userService.updateUser(user));
    }

    @DeleteMapping("/{userId}") // Удаление пользователя по Id
    public HttpStatus deleteUserBuId(@PathVariable long userId) {
        return userService.deleteUser(userId);
    }
}
