package ru.practicum.shareit.item.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
