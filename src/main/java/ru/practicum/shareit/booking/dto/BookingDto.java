package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookingDto {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotNull
    private Long itemId;
    private long bookerId;
    @NotNull(message = "Не указано время начала бронирования")
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Не указано время конца бронирования")
    @FutureOrPresent(message = "Время конца бронирования не может быть в прошлом")
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
