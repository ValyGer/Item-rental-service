package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.model.ItemRequest;


@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(source = "id", target = "id")
    ItemRequest toItemRequest(ItemRequestDto  itemRequestDto);

    @Mapping(source = "id", target = "id")
    ItemRequestDto toItemRequestDto(ItemRequest  itemRequest);
}

