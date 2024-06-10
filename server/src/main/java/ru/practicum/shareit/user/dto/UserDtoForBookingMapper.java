package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserDtoForBookingMapper {

    @Mapping(source = "id", target = "id")
    UserDtoForBooking userDtoForBooking(User user);
}
