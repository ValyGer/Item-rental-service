package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDto {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

    public ItemDto(String name, String description, Boolean available, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
