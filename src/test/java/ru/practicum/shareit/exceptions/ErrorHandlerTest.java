package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void errorValidation() {
        ValidationException exception = new ValidationException("Пользователь не может оставить отзыв на вещь " +
                "если не брал ее в аренду.");
        ErrorResponse errorResponse = errorHandler.errorValidation(exception);

        assertEquals(exception.getMessage(), errorResponse.getError());
    }

    @Test
    void errorEmailAlreadyExists() {
        ConflictException exception = new ConflictException("Пользователь с такой почтой уже существует");
        ErrorResponse errorResponse = errorHandler.errorEmailAlreadyExists(exception);

        assertEquals(exception.getMessage(), errorResponse.getError());
    }

    @Test
    void notFoundException() {
        NotFoundException exception = new NotFoundException("Пользователь не найден");
        ErrorResponse errorResponse = errorHandler.notFoundException(exception);

        assertEquals(exception.getMessage(), errorResponse.getError());
    }
}