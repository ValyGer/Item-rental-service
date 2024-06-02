package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void toUserTest() {
        UserDto userDto = new UserDto(99L, "Mail", "mail@mail.ru");
        User userAfterTransformation = userMapper.toUser(userDto);

        assertEquals(userAfterTransformation.getId(), userDto.getId());
        assertEquals(userAfterTransformation.getUserName(), userDto.getName());
        assertEquals(userAfterTransformation.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserDtoTest() {
        User userInitial = new User(1L, "Name", "name@mail.ru");
        UserDto userDtoAfterTransformation = userMapper.toUserDto(userInitial);

        assertEquals(userInitial.getId(), userDtoAfterTransformation.getId());
        assertEquals(userInitial.getUserName(), userDtoAfterTransformation.getName());
        assertEquals(userInitial.getEmail(), userDtoAfterTransformation.getEmail());
    }
}