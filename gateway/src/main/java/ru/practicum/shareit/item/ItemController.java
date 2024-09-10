package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Optional;

@Controller
@RequestMapping(path = "/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обработан POST item запрос. Пользователем с ID {}", userId);
        if (itemRequestDto.getAvailable() == null || (itemRequestDto.getName() == null ||
                itemRequestDto.getName().isBlank()) || (itemRequestDto.getDescription() == null ||
                itemRequestDto.getDescription().isBlank())) {
            throw new ValidationException("Нельзя создать объект без указания полей - available, name, description");
        }
        return itemClient.create(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                         @PathVariable Long itemId, @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обработан PATCH item запрос. /items/{}", userId);
        if (itemRequestDto.getDescription() != null && itemRequestDto.getDescription().isBlank()) {
            throw new ValidationException("Описание не может быть пустым");
        }
        if (itemRequestDto.getName() != null && itemRequestDto.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        return itemClient.update(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId) {
        log.info("Обработан GET item запрос. /items/{}", itemId);
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET items запрос. /items Пользователя с ID - {}", userId);
        return itemClient.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam Optional<String> text,
                                              @RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET items запрос. /items/search с текстом - {}", text);
        return itemClient.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComments(@PathVariable Long itemId,
                                                 @RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                                 @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Обработан POST /itemId/comment запрос.");
        ResponseEntity<Object> objectResponseEntity = itemClient.createComments(itemId, userId, commentRequestDto);
        log.info(objectResponseEntity.toString());
        return objectResponseEntity;
    }
}
