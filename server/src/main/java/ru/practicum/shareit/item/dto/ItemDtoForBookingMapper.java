package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemDtoForBookingMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "name", target = "name")
    ItemDtoForBooking toItemDtoForBooking(Item item);
}
