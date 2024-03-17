package ru.practicum.shareit.item.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MemoryItemStorageImpl implements ItemStorage {

    private final HashMap<Long, Item> items = new HashMap<>(); // Общий список вещей
    private static long generateItemId = 0L;

    // Создание вещи
    public Item createItem(Long userId, Item item) {
        item.setItemId(++generateItemId);
        log.info("Вещь успешно добавлена");
        item.setOwner(userId);
        items.put(item.getItemId(), item);
        return item;
    }

    // Обновление вещи
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item saved = items.get(itemId);
        if (saved != null) {
            if (saved.getOwner() == userId) {
                if (item.getName() != null) {
                    log.info("Название вещи с Id = {} обновлено", itemId);
                    saved.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    log.info("Описание вещи с Id = {} обновлено", itemId);
                    saved.setDescription(item.getDescription());
                }
                if (item.getIsAvailable() != null) {
                    log.info("Статус вещи с Id = {} обновлен", itemId);
                    saved.setIsAvailable(item.getIsAvailable());
                }
                items.put(saved.getItemId(), saved);
                return saved;
            } else {
                log.info("Данной вещи с Id = {} нет у пользователя", itemId);
                throw new NotFoundException("Вещи с указанным Id не принадлежит данному пользователю");
            }
        } else {
            log.info("Вези с Id = {} в базе нет", itemId);
            throw new NotFoundException("Вещи с указанным Id не нашлось");
        }
    }

    // Получение списка вещей пользователя
    public List<Item> getAllItemsUser(Long userId) {
        List<Item> itemOfUser = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner() == userId) {
                itemOfUser.add(item);
            }
        }
        log.info("Список вещей успешно выведен");
        return itemOfUser;
    }

    // Получение вещи по ID
    public Item getItemsById(Long userId, Long itemId) {
        log.info("Вещь успешно возвращена");
        return items.get(itemId);
    }

    // Поиск вещи по строке в названии и в описании
    public List<Item> searchAvailableItems(String text) {
        String lowerCaseText = text.toLowerCase();
        log.info("Список доступных вещей в которых встречается запрос {} успешно выведен", text);
        return items.values().stream()
                .filter(item -> item.getIsAvailable().equals(true))
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseText)) |
                        (item.getDescription().toLowerCase().contains(lowerCaseText)))
                .collect(Collectors.toList());
    }
}
