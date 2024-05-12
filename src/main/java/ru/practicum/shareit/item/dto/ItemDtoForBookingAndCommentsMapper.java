package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemDtoForBookingAndCommentsMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "isAvailable", target = "available")
    ItemDtoForBookingAndComments toItemDtoForBookingAndComments(Item item);
}
