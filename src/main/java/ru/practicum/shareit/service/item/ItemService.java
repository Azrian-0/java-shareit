package ru.practicum.shareit.service.item;

import ru.practicum.shareit.dto.item.CommentRequestDto;
import ru.practicum.shareit.dto.item.ItemRequestDto;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item create(Long userId, ItemRequestDto itemRequestDto);

    Item update(Long userId, Long itemId, ItemRequestDto itemRequestDto);

    List<Item> getItemsByUser(Long userId);

    Item getById(Long itemId);

    List<Item> search(Optional<String> text, Long userId);

    Comment createComment(CommentRequestDto commentRequestDto, Long userId, Long itemId);
}