package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
            user.getUserId(),
            user.getUserName(),
            user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }
    public static User toUser(long userId, UserDto userDto) {
        return new User(
                userId,
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
