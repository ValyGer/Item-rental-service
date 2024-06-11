package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDtoForBooking {
    private Long id;
    private String name;

    public ItemDtoForBooking(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}