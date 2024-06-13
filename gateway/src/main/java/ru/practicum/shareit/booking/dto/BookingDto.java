package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;

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
    private Long bookerId;
    @NotNull(message = "Не указано время начала бронирования")
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Не указано время конца бронирования")
    @FutureOrPresent(message = "Время конца бронирования не может быть в прошлом")
    private LocalDateTime end;
    private Status status;
}