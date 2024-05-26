package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemDtoForRequestMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "isAvailable", target = "available")
    @Mapping(source = "request", target = "requestId")
    ItemDtoForRequest toItemDtoForRequest(Item item);

    @Mapping(source = "id", target = "itemId")
    @Mapping(source = "available", target = "isAvailable")
    @Mapping(source = "requestId", target = "request")
    Item toItem(ItemDtoForRequest itemDtoForRequest);
}