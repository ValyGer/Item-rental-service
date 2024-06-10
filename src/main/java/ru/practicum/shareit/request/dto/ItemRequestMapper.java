package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDtoForRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", uses = ItemDtoForRequestMapper.class)
public interface ItemRequestMapper {

    @Mapping(source = "id", target = "id")
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "requester", target = "ownerId", qualifiedBy = RequesterToOwnerIdDto.class)
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    @RequesterToOwnerIdDto
    static Long getIdRequester(User requester) {
        return requester.getId();
    }
}