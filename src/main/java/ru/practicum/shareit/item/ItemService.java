package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndComments;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDtoForBookingAndComments> getAllItemsUser(Long userId);

    Item getItemsById(Long itemId);

    List<Item> searchAvailableItems(String text);

    ItemDtoForBookingAndComments getItemWithBooker(long itemId, long ownerId);

    Comment addComment(long userId, long itemId, CommentDto commentDto);
}
