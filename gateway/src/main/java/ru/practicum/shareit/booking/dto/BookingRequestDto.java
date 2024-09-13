package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.practicum.shareit.validation.SecondOrder;

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
    @AssertFalse(groups = {SecondOrder.class})
    public boolean isStartEqualsEnd() {
        return start.equals(end);
    }

    @JsonIgnore
    @AssertTrue(groups = {SecondOrder.class})
    public boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
