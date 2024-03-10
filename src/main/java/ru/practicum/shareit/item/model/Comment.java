package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {
    private long commentId; // идентификатор комментария
    private String text; // содержание комментария
    private long itemId; // вещь на которую оставлен комментарий
    private long authorId; // номер пользователя которому принадлежит комментарий
    private LocalDateTime create; // дата и время создания комментария
}
