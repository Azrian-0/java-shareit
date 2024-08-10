package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemInterface itemService;

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.info("Обработан POST item запрос. Пользователем с ID {}", userId);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Обработан PATCH item запрос. /items/{}", userId);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        log.info("Обработан GET item запрос. /items/{}", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<Item> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обработан GET items запрос. /items Пользователя с ID - {}", userId);
        return itemService.getByUserId(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam Optional<String> text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обработан GET items запрос. /items/search с текстом - {}", text);
        return itemService.search(text, userId);
    }
}