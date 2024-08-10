package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemInterface {

    private final ItemStorage inMemoryItemStorage;

    @Override
    public Item create(Long userId, ItemDto itemDto) {
        return inMemoryItemStorage.create(userId, itemDto);
    }

    @Override
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        return inMemoryItemStorage.update(userId, itemId, itemDto);
    }

    @Override
    public Item getById(Long itemId) {

        Optional<Item> itemOptional = inMemoryItemStorage.getById(itemId);

        if (itemOptional.isEmpty()) {
            throw new NotFoundException(String.format("Предмет с ID %d не найден", itemId));
        }
        return itemOptional.get();
    }

    @Override
    public List<Item> getByUserId(Long userId) {
        return inMemoryItemStorage.getByUserId(userId);
    }

    @Override
    public List<Item> search(Optional<String> text, Long userId) {
        return inMemoryItemStorage.search(text, userId);
    }
}