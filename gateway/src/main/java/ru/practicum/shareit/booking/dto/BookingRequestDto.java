package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    @JsonIgnore
    @AssertFalse
    public boolean isStartEqualsEnd() {
        return start.equals(end);
    }

    @JsonIgnore
    @AssertTrue
    public boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
