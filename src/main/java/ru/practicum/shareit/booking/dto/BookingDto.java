package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;
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
public class BookingDto {
    private Long id;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    public BookingDto(Item item, User booker, LocalDateTime start, LocalDateTime end, Status status) {
        this.id = item.getItemId();
        this.booker = booker;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
