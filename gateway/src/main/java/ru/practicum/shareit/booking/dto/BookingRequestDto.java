package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

@Data
public class BookingRequestDto {

    private Long itemId;

    @FutureOrPresent
    private String start;

    private String end;
}
