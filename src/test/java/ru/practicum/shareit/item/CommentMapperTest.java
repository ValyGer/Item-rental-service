package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapperImpl commentMapper;

    @Test
    void toCommentDto() {
        User user = new User(1L, "Mail", "mail@mail.ru");
        Comment comment = new Comment("text", user, LocalDateTime.now());

        CommentDto commentDto = commentMapper.toCommentDto(comment);

        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getAuthor().getUserName(), commentDto.getAuthorName());
        assertEquals(comment.getCreate(), commentDto.getCreated());
    }

    @Test
    void getNameAuthor() {
        User user = new User(99L, "Mail", "mail@mail.ru");
        String userName = user.getUserName();

        assertEquals("Mail", userName);
    }

    @Test
    void toComment() {
        CommentDto commentDto = new CommentDto(1L, "text", "author Name", LocalDateTime.now());

        Comment comment = commentMapper.toComment(commentDto);

        assertEquals(commentDto.getText(), comment.getText());
    }
}