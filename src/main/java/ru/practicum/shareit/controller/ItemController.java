package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.CommentRequestDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemRequestDto;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.item.ItemService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDto itemRequest) {
        log.info("Обработан POST item запрос. Пользователем с ID {}", userId);
        return ItemMapper.map(itemService.create(userId, itemRequest));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemRequestDto itemRequest) {
        log.info("Обработан PATCH item запрос. /items/{}", userId);
        return ItemMapper.map(itemService.update(userId, itemId, itemRequest));
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("Обработан GET item запрос. /items/{}", itemId);
        return ItemMapper.map(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обработан GET items запрос. /items Пользователя с ID - {}", userId);
        return itemService.getItemsByUser(userId).stream().map(ItemMapper::map).toList();
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam Optional<String> text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обработан GET items запрос. /items/search с текстом - {}", text);
        return itemService.search(text, userId).stream().map(ItemMapper::map).toList();
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComments(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Обработан POST /itemId/comment запрос.");
        return CommentMapper.map(itemService.createComment(commentRequestDto, userId, itemId));
    }
}