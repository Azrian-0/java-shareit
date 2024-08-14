package ru.practicum.shareit.storage.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.storage.user.UserStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private final UserStorage inMemoryUserStorage;
    private final Map<Long, Map<Long, Item>> items = new HashMap<>();
    private long itemId = 0;

    @Override
    public Item create(Long userId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null || (itemDto.getName() == null ||
                itemDto.getName().isBlank()) || (itemDto.getDescription() == null ||
                itemDto.getDescription().isBlank())) {
            throw new ValidationException("Пустые поля класса");
        }

        if (!inMemoryUserStorage.isUserContains(userId)) {
            throw new NotFoundException("Пользователя не существует");
        }
        Item newItem = ItemMapper.map(itemDto);
        newItem.setId(getNextId());
        newItem.setOwnerId(userId);
        Map<Long, Item> itemList = items.getOrDefault(userId, new HashMap<>());
        itemList.put(newItem.getId(), newItem);
        items.put(userId, itemList);
        return newItem;
    }

    @Override
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        Map<Long, Item> itemsForUpdate = items.getOrDefault(userId, new HashMap<>());
        if (getById(itemId).isEmpty()) {
            throw new NotFoundException("Предмета с ID - " + itemId + ", не существует");
        }
        if (!inMemoryUserStorage.isUserContains(userId)) {
            throw new NotFoundException("Пользователя не существует");
        }
        if (!itemsForUpdate.containsKey(itemId)) {
            throw new NotFoundException(String.format("Пользователь с ID - %d, не владеет предметом - %d", userId, itemId));
        }
        Item itemForUpdate = itemsForUpdate.get(itemId);
        if (itemDto.getDescription() != null) {
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }
        itemsForUpdate.put(itemId, itemForUpdate);
        items.put(userId, itemsForUpdate);
        return itemForUpdate;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return items.values().stream()
                .flatMap(itemMap -> itemMap.values().stream())
                .filter(item -> item.getId().equals(itemId))
                .findFirst();
    }

    @Override
    public List<Item> getByUserId(Long userId) {
        if (!inMemoryUserStorage.isUserContains(userId)) {
            throw new NotFoundException("Пользователя не существует");
        }
        return items.get(userId).values().stream().toList();
    }

    @Override
    public List<Item> search(Optional<String> text, Long userId) {
        if (text.isEmpty() || text.get().isBlank()) {
            return new ArrayList<>();
        }
        String searchText = text.get().toLowerCase();
        return items.values().stream().flatMap(
                itemMap -> itemMap.values().stream()).filter(Item::getAvailable).filter(
                item -> item.getName().toLowerCase().contains(searchText) ||
                        item.getDescription().toLowerCase().contains(searchText)).toList();
    }

    private long getNextId() {
        return ++itemId;
    }
}