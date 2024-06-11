package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookingDto {
    @EqualsAndHashCode.Exclude
    private Long id;
    private Long itemId;
    private long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    public BookingDto(Long itemId, long bookerId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.start = start;
        this.end = end;
    }

    public BookingDto(Long itemId, long bookerId, LocalDateTime start, LocalDateTime end, Status status) {
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
