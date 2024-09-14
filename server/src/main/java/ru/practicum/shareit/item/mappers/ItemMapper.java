package ru.practicum.shareit.item.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item map(ItemRequestDto itemRequestDto) {
        Item item = new Item();
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        return item;
    }

    public static ItemDto map(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(item.getAvailable());
        itemDto.setId(item.getId());
        itemDto.setOwnerId(item.getOwner().getId());
        if (!item.getComments().isEmpty()) {
            itemDto.setComments(item.getComments().stream().map(CommentMapper::map).toList());
        } else {
            itemDto.setComments(new ArrayList<>());
        }
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }
}
