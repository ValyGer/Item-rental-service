package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getItemName(),
                item.getDescription(),
                item.getStatus(),
                item.getRequest()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getItemName(),
                itemDto.getDescription(),
                itemDto.getStatus(),
                itemDto.getRequest()
        );
    }
}
