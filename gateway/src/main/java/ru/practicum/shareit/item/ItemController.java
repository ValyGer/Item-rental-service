package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping // Создание новой вещи
    public ResponseEntity<Object> createItem(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}") // Обновление информации о вещи
    public ResponseEntity<Object> updateItemById(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long itemId,
                                                 @RequestBody ItemDto itemDto) {
        return itemClient.updateItemById(userId, itemId, itemDto);
    }

    @GetMapping // Получение списка вещей пользователя
    public ResponseEntity<Object> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getAllItemsUser(userId);
    }

    @GetMapping("/{itemId}") // Получение вещи по Id
    public ResponseEntity<Object> getItemWithBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable long itemId) {
        return itemClient.getItemWithBooker(userId, itemId);
    }

    @GetMapping("/search") // Поиск вещи по строке text
    public ResponseEntity<Object> searchAvailableItems(@RequestParam String text) {
        return itemClient.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment") // Добавление комментариев
    public ResponseEntity<Object> addCommentToItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                   @Valid @RequestBody CommentDto commentDto,
                                                   @Positive @PathVariable long itemId) {
        return itemClient.addCommentToItem(itemId, commentDto, userId);
    }
}
