package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemRequestDto {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank
    private String description;
    private Long ownerId;
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;

    public ItemRequestDto(String description, Long ownerId, LocalDateTime created) {
        this.description = description;
        this.ownerId = ownerId;
        this.created = created;
    }

    public ItemRequestDto(Long id, String description, Long ownerId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.ownerId = ownerId;
        this.created = created;
    }

    public ItemRequestDto(Long id, String description, Long ownerId, LocalDateTime created, List<ItemDtoForRequest> items) {
        this.id = id;
        this.description = description;
        this.ownerId = ownerId;
        this.created = created;
        this.items = items;
    }
}
