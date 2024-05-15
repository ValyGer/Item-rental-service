package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BookingLastNextDto {
    private long id;
    private long bookerId;
}
