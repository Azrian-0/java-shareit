package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("${shareIt.headers.userHeader}") Long userId,
                          @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обработан POST item запрос. Пользователем с ID {}", userId);
        return ItemMapper.map(itemService.create(userId, itemRequestDto));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("${shareIt.headers.userHeader}") Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обработан PATCH item запрос. /items/{}", userId);
        return ItemMapper.map(itemService.update(userId, itemId, itemRequestDto));
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("Обработан GET item запрос. /items/{}", itemId);
        return ItemMapper.map(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET items запрос. /items Пользователя с ID - {}", userId);
        return itemService.getItemsByUser(userId).stream().map(ItemMapper::map).toList();
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam Optional<String> text,
                                     @RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET items запрос. /items/search с текстом - {}", text);
        return itemService.search(text, userId).stream().map(ItemMapper::map).toList();
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComments(@PathVariable Long itemId,
                                     @RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                     @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Обработан POST /itemId/comment запрос.");
        return CommentMapper.map(itemService.createComment(commentRequestDto, userId, itemId));
    }
}
