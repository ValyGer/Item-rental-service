package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
            user.getUserName(),
            user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getUserName(),
                userDto.getEmail()
        );
    }
}
