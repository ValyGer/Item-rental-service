package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author", target = "authorName", qualifiedBy = CommentToCommentDto.class)
    CommentDto toCommentDto(Comment comment);

    @CommentToCommentDto
    static String getNameAuthor(User author) {
        return author.getUserName();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "create", ignore = true)
    Comment toComment(CommentDto commentDto);
}
