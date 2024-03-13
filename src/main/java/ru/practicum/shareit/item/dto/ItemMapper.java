package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "available")
    ItemDto toItemDto(Item item);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "available", target = "status")
    Item toItem(ItemDto itemDto);
}
