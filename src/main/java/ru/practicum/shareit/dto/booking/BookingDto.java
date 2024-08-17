package ru.practicum.shareit.dto.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class BookingDto {

    private Long id;

    private ItemForBookingDto item;

    private UserDto booker;

    private BookingStatus status;

    private LocalDateTime start;

    private LocalDateTime end;
}
