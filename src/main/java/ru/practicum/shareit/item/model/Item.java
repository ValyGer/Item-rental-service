package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class Item {

    private long itemId; // идентификатор в базе
    private String itemName; // наименование
    private String description; // описание
    private long owner; // номер пользователя которому принадлежит вещь
    private Boolean status; // статус вещи: доступна не доступна
    private ItemRequest request; // если вещь создана по запросу, то тут будет ссылка на номер этого запроса

    public Item(String itemName, String description, Boolean status) {
        this.itemName = itemName;
        this.description = description;
        this.status = status;
    }
}
