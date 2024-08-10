package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor
public class ItemMapper {
    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder().name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable()).build();
    }
}