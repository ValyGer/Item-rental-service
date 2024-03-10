package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemRequest {
    private long itemRequestId; //идентификатор запроса
    private String description; // описание запроса
    private User requestor; // пользователь, который создал запроса
    private LocalDateTime create; // дата и время создания запроса
}
