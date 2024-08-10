package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item create(Long userId, ItemDto itemDto);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    Optional<Item> getById(Long itemId);

    List<Item> getByUserId(Long userId);

    List<Item> search(Optional<String> text, Long userId);
}