package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping // Добавление нового запроса вещи
    public ResponseEntity<ItemRequestDto> createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok().body(itemRequestMapper
                .toItemRequestDto(itemRequestService.createItemRequest(itemRequestDto, userId)));
    }

    @GetMapping // Получение пользователем всех запросов
    public ResponseEntity<List<ItemRequestDto>> getAllItemRequestOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                        @RequestParam(required = false, defaultValue = "20") Integer size) {
        return ResponseEntity.ok().body(itemRequestService.getAllItemRequestOfUser(userId, from, size)
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/all") // Получение пользователем списка запросов созданного другими пользователями
    public ResponseEntity<List<ItemRequestDto>> getAllItemRequestOfOtherUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                              @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                              @RequestParam(required = false, defaultValue = "20") Integer size) {
        return ResponseEntity.ok().body(itemRequestService.getAllItemRequestOfOtherUsers(userId, from, size)
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{requestId}") // Получение информации о бронировании
    public ResponseEntity<ItemRequestDto> getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @PathVariable long requestId) {
        return ResponseEntity.ok().body(itemRequestMapper
                .toItemRequestDto(itemRequestService.getItemRequest(requestId, userId)));
    }
}
