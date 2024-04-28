package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private long id; // идентификатор комментария
    @NotBlank
    private String text; // содержание комментария
    private String authorName; // номер пользователя которому принадлежит комментарий
    private LocalDateTime created;

    public CommentDto(long id, String text, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}
