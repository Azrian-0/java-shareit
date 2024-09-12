package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.Collections;

@Controller
@RequestMapping(path = "/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<Object> create(@RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обработан POST item запрос. Пользователем с ID {}", userId);
        return itemClient.create(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    @Validated(Update.class)
    public ResponseEntity<Object> update(@RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                         @PathVariable Long itemId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обработан PATCH item запрос. /items/{}", userId);
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
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET items запрос. /items/search с текстом - {}", text);
        if (ObjectUtils.isEmpty(text)) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    @Validated(Create.class)
    public ResponseEntity<Object> createComments(@PathVariable Long itemId,
                                                 @RequestHeader("${shareIt.headers.userHeader}") Long userId,
                                                 @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Обработан POST /itemId/comment запрос.");
        ResponseEntity<Object> objectResponseEntity = itemClient.createComments(itemId, userId, commentRequestDto);
        log.info(objectResponseEntity.toString());
        return objectResponseEntity;
    }
}
