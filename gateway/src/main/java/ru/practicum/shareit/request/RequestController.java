package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping // Добавление нового запроса вещи
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return requestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping // Получение пользователем всех запросов
    public ResponseEntity<Object> getAllItemRequestOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        return requestClient.getAllItemRequestOfUser(userId, from, size);
    }

    @GetMapping("/all") // Получение пользователем списка запросов созданного другими пользователями
    public ResponseEntity<Object> getAllItemRequestOfOtherUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "20") Integer size) {
        return requestClient.getAllItemRequestOfOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}") // Получение информации о бронировании
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable long requestId) {
        return requestClient.getItemRequestById(requestId, userId);
    }
}