package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemRequestDto {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank
    private String description;
    private Long requestorId;
    private LocalDateTime create;

    public ItemRequestDto(String description) {
        this.description = description;
    }

    public ItemRequestDto(String description, User requestor, LocalDateTime create) {
        this.description = description;
        this.requestorId = requestor.getId();
        this.create = create;
    }

    public ItemRequestDto(Long id, String description, Long requestorId, LocalDateTime create) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.create = create;
    }
}
