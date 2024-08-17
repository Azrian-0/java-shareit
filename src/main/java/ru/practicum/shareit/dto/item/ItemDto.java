package ru.practicum.shareit.dto.item;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long ownerId;

    private Long lastBooking;

    private Long nextBooking;

    private List<CommentDto> comments;
}
