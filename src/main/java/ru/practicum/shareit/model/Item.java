package ru.practicum.shareit.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}