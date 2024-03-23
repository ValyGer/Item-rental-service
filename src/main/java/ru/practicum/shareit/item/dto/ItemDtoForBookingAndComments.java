package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDtoForBookingAndComments {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingLastNextDto lastBooking;
    private BookingLastNextDto nextBooking;

    public ItemDtoForBookingAndComments(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}


