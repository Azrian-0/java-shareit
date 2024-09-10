package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestData;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(Long userId, ItemRequestData itemRequestData);

    List<ItemRequest> getAllByUser(Long userId);

    List<ItemRequest> getAllByOtherUsers(Long userId);

    ItemRequest getById(Long itemRequestId);
}
