package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoForBookingAndComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    List<Item> getAllItemsUser(Long userId);

    Item getItemsById(Long userId, Long itemId);

    List<Item> searchAvailableItems(String text);

    ItemDtoForBookingAndComments getItemWithBooker(long itemId, long ownerId);
}
