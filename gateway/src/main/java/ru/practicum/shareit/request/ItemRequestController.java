package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDataDto;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestBody ItemRequestDataDto itemRequestData) {
        log.info("Обработан POST request запрос. Пользователем с ID {}", userId);
        if (itemRequestData.getDescription().isBlank()) {
            throw new ValidationException("Нельзя создать объект без указания поля - description");
        }
        return itemRequestClient.create(userId, itemRequestData);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET request запрос. Пользователем с ID {}", userId);
        return itemRequestClient.getAllByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOthersUsersRequests(@RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET /requests/all запрос. Пользователем с ID {}", userId);
        return itemRequestClient.getAllByOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId) {
        log.info("Обработан GET /requests/{} запрос.", requestId);
        return itemRequestClient.getById(requestId);
    }
}
