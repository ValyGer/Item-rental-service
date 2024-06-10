package ru.practicum.shareit.request;

import lombok.*;

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
}
