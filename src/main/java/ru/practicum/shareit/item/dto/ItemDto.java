package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {

    private String name;

    private String description;

    @NotNull(message = "Доступность для аренды не может быть null")
    private Boolean available;
}