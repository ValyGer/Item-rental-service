package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDtoForRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
