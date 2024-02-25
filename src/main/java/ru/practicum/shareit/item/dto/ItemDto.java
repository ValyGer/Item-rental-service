package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    private String itemName;
    private String description;
    private Boolean status;
    private ItemRequest request;

    public ItemDto(String itemName, String description, Boolean status, ItemRequest request) {
        this.itemName = itemName;
        this.description = description;
        this.status = status;
        this.request = request;
    }
}
