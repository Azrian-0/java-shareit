package ru.practicum.shareit.storage.item;

import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item create(Long userId, ItemDto itemDto);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    Optional<Item> getById(Long itemId);

    List<Item> getByUserId(Long userId);

    List<Item> search(Optional<String> text, Long userId);
}