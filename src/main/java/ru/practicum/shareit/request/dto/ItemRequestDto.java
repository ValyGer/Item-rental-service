package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemRequestDto {
    private String description;
    private Long requestorId;
    private LocalDateTime create;

    public ItemRequestDto(String description, User requestor, LocalDateTime create) {
        this.description = description;
        this.requestorId = requestor.getUserId();
        this.create = create;
    }
}
