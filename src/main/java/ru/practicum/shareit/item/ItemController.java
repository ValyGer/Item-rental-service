package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor

public class ItemController {

    private final ItemService itemService;

    @PostMapping // Создание новой вещи
    public ItemDto createItem(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                    @Valid @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemService.createItem(userId, item));
    }

    @PatchMapping("/{itemId}") // Обновление информации о вещи
    public ItemDto updateItem(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, item));
    }

    @GetMapping // Получение списка вещей пользователя
    public List<ItemDto> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsUser(userId)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}") // Получение вещи по Id
    public ItemDto getItemsById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getItemsById(userId, itemId));
    }

}
