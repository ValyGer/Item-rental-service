package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.dto.UserDtoForBookingMapperImpl;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoForBookingMapperTest {

    private UserDtoForBookingMapperImpl userDtoForBookingMapper = new UserDtoForBookingMapperImpl();

    @Test
    void userDtoForBookingTest() {
        User userInitial = new User(1L, "Name", "name@mail.ru");
        UserDtoForBooking userDtoAfterTransformation = userDtoForBookingMapper.userDtoForBooking(userInitial);

        assertEquals(userInitial.getId(), userDtoAfterTransformation.getId());
    }
}