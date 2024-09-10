package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestData;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mappers.RequestMapper;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestBody ItemRequestData itemRequestData) {
        log.info("Обработан POST /requests запрос. Пользователем с ID {}", userId);
        return RequestMapper.map(itemRequestService.create(userId, itemRequestData));
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsByUser(@RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET /requests запрос. Пользователем с ID {}", userId);
        return itemRequestService.getAllByUser(userId).stream().map(RequestMapper::map).toList();
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOthersUsersRequests(@RequestHeader("${shareIt.headers.userHeader}") Long userId) {
        log.info("Обработан GET /requests/all запрос. Пользователем с ID {}", userId);
        return itemRequestService.getAllByOtherUsers(userId).stream().map(RequestMapper::map).toList();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId) {
        log.info("Обработан GET /requests/{} запрос.", requestId);
        return RequestMapper.map(itemRequestService.getById(requestId));
    }
}
