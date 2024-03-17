package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.user.dto.UserDtoForBooking;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookingDtoWithItem {
    @EqualsAndHashCode.Exclude
    private Long id;
    private ItemDtoForBooking item;
    private UserDtoForBooking booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    public BookingDtoWithItem(Long id, ItemDtoForBooking item, UserDtoForBooking booker, LocalDateTime start, LocalDateTime end, Status status) {
        this.id = id;
        this.item = item;
        this.booker = booker;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}