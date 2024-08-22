package ru.practicum.shareit.dto.item;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestDto {

    private String name;

    private String description;

    private Boolean available;
}
