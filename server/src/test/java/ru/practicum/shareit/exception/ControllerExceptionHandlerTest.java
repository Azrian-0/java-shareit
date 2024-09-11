package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.handler.ControllerExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void handleValidationException_ShouldReturnBadRequest() {
        ValidationException exception = new ValidationException("Validation failed");
        ErrorResponse response = handler.handle(exception);

        assertEquals("Validation failed", response.getError());
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound() {
        NotFoundException exception = new NotFoundException("Resource not found");
        ErrorResponse response = handler.handle(exception);

        assertEquals("Resource not found", response.getError());
    }

    @Test
    void handleConflictException_ShouldReturnConflict() {
        ConflictException exception = new ConflictException("Conflict occurred");
        ErrorResponse response = handler.handle(exception);

        assertEquals("Conflict occurred", response.getError());
    }

    @Test
    void handleInternalServerException_ShouldReturnInternalServerError() {
        InternalServerException exception = new InternalServerException("Internal server error");
        ErrorResponse response = handler.handle(exception);

        assertEquals("Internal server error", response.getError());
    }
}
