package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

@Data
public class ItemRequestDto {

    @NotBlank(groups = {Create.class})
    @Size(groups = {Create.class, Update.class}, max = 30)
    private String name;

    @NotBlank(groups = {Create.class})
    @Size(groups = {Create.class, Update.class}, max = 255)
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long requestId;
}
