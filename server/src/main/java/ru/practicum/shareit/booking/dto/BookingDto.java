package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;

    private ItemForBookingDto item;

    private UserDto booker;

    private BookingStatus status;

    private LocalDateTime start;

    private LocalDateTime end;
}
