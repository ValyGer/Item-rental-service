package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {
    private long bookingId;
    private Item item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}
