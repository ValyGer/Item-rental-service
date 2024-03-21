package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

public interface ItemDtoForBookingAndCommentsMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "name", target = "name")
    ItemDtoForBookingAndComments toItemDtoForBookingAndComments(Item item);
}
