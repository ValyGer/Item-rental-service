package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDtoForBookingAndComments {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Booking lastBooking;
    private Booking nextBooking;

    public ItemDtoForBookingAndComments(Long id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }
}


