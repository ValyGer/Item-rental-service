package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
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
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping // Создание новой вещи
    public ResponseEntity<ItemDto> createItem(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Valid @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return ResponseEntity.ok().body(itemMapper.toItemDto(itemService.createItem(userId, item)));
    }

    @PatchMapping("/{itemId}") // Обновление информации о вещи
    public ResponseEntity<ItemDto> updateItemById(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long itemId,
                                              @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return ResponseEntity.ok().body(itemMapper.toItemDto(itemService.updateItem(userId, itemId, item)));
    }

    @GetMapping // Получение списка вещей пользователя
    public ResponseEntity<List<ItemDtoForBookingAndComments>> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return ResponseEntity.ok().body(itemService.getAllItemsUser(userId));
    }

    @GetMapping("/{itemId}") // Получение вещи по Id
    public ResponseEntity<ItemDtoForBookingAndComments> getItemWithBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                          @PathVariable long itemId) {
        return ResponseEntity.ok().body(itemService.getItemWithBooker(itemId, userId));
    }

    @GetMapping("/search") // Поиск вещи по строке text
    public ResponseEntity<List<ItemDto>> searchAvailableItems(@RequestParam String text) {
        return ResponseEntity.ok().body(itemService.searchAvailableItems(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{itemId}/comment") // Добавление комментариев
    public ResponseEntity<CommentDto> addCommentToItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @Valid @RequestBody CommentDto commentDto,
                                                       @PathVariable long itemId) {
        Comment comment = commentMapper.toComment(commentDto);
        return ResponseEntity.ok().body(commentMapper.toCommentDto(itemService.addComment(userId, itemId, comment)));
    }
}
