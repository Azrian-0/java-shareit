package ru.practicum.shareit.dto.booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemForBookingDto {

    private Long id;

    private String name;
}
