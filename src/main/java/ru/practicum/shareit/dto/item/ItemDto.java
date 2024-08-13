package ru.practicum.shareit.dto.item;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ItemDto {

    private String name;

    private String description;

    @NotNull(message = "Доступность для аренды не может быть null")
    private Boolean available;
}