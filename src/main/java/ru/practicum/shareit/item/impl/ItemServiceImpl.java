package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage memoryUserStorage;

    public Item createItem(Long userId, Item item) {
        if (memoryUserStorage.IsUserFound(userId)) {
            log.debug("Вызван метод создания новой вещи");
            return itemStorage.createItem(userId, item);
        } else {
            log.info("Пользователь с идентификатором id = {} не найден", userId);
            throw new NotFoundException("Пользователь с идентификатором id не найден");
        }
    }

    public Item updateItem(Long userId, Long itemId, Item item) {
        if (memoryUserStorage.IsUserFound(userId)) {
            log.debug("Вызван метод обновления существующей вещи");
            return itemStorage.updateItem(userId, itemId, item);
        } else {
            log.info("Пользователь с идентификатором id = {} не найден", userId);
            throw new NotFoundException("Пользователь с идентификатором id не найден");
        }
    }

    public List<Item> getAllItemsUser(Long userId) {
        if (memoryUserStorage.IsUserFound(userId)) {
            log.debug("Вызван метод вывода списка вещей пользователя");
            return itemStorage.getAllItemsUser(userId);
        } else {
            log.info("Пользователь с идентификатором id = {} не найден", userId);
            throw new NotFoundException("Пользователь с идентификатором id не найден");
        }
    }

    public Item getItemsById(Long userId, Long itemId) {
        log.debug("Вызван метод вывода списка вещей пользователя");
        return itemStorage.getItemsById(userId, itemId);
    }

    public List<Item> searchAvailableItems(String text) {
        if (text.isBlank()) {
            log.debug("Передан пустой запрос, возвращен пустой список");
            return new ArrayList<>();
        } else {
            log.debug("Вызван метод поиска доступной вещи");
            return itemStorage.searchAvailableItems(text);
        }
    }
}
