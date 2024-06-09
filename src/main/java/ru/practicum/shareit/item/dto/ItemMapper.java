package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "isAvailable", target = "available")
    @Mapping(source = "request.id", target = "requestId")
    ItemDto toItemDto(Item item);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "available", target = "isAvailable")
    @Mapping(source = "itemDto.requestId", target = "request.id")
    Item toItem(ItemDto itemDto);
}