package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private long itemRequestId; //идентификатор запроса
    private String description; // описание запроса
    private User requestor; // пользователь, который создал запроса
    private LocalDateTime create; // дата и время создания запроса
}
