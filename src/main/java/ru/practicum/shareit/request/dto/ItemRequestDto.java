package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDto {
    private String description;
    private User requestor;
    private LocalDateTime create;

    public ItemRequestDto(String description, User requestor, LocalDateTime create) {
        this.description = description;
        this.requestor = requestor;
        this.create = create;
    }
}
