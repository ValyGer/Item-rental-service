package ru.practicum.shareit.item.exceptions;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
