package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.item.ItemService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Обработан POST item запрос. Пользователем с ID {}", userId);
        return ItemMapper.map(itemService.create(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Обработан PATCH item запрос. /items/{}", userId);
        return ItemMapper.map(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@PathVariable Long itemId) {
        log.info("Обработан GET item запрос. /items/{}", itemId);
        return ItemMapper.map(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemResponseDto> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обработан GET items запрос. /items Пользователя с ID - {}", userId);
        return itemService.getByUserId(userId).stream().map(ItemMapper::map).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestParam Optional<String> text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обработан GET items запрос. /items/search с текстом - {}", text);
        return itemService.search(text, userId).stream().map(ItemMapper::map).collect(Collectors.toList());
    }
}