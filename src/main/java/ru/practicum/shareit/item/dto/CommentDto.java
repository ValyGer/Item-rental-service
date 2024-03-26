package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private long commentId; // идентификатор комментария
    private String text; // содержание комментария
    private User author; // номер пользователя которому принадлежит комментарий
    private LocalDateTime create;

    public CommentDto(long commentId, String text, User author, LocalDateTime create) {
        this.commentId = commentId;
        this.text = text;
        this.author = author;
        this.create = create;
    }
}
