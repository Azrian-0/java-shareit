package ru.practicum.shareit.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.item.CommentRequestDto;
import ru.practicum.shareit.dto.item.ItemRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.repository.booking.BookingRepository;
import ru.practicum.shareit.repository.item.CommentRepository;
import ru.practicum.shareit.repository.item.ItemRepository;
import ru.practicum.shareit.repository.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item create(Long userId, ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getAvailable() == null || (itemRequestDto.getName() == null ||
                itemRequestDto.getName().isBlank()) || (itemRequestDto.getDescription() == null ||
                itemRequestDto.getDescription().isBlank())) {
            log.error("Нельзя создать объект без указания полей - available, name, description");
            throw new ValidationException("Нельзя создать объект без указания полей - available, name, description");
        }

        if (!userRepository.existsById(userId)) {
            log.error("Error такого пользователя не существует");
            throw new NotFoundException("Error такого пользователя не существует");
        }
        Item newItem = ItemMapper.map(itemRequestDto);
        newItem.setOwner(userRepository.findById(userId).get());
        return itemRepository.save(newItem);
    }

    @Override
    @Transactional
    public Item update(Long userId, Long itemId, ItemRequestDto itemRequestDto) {
        Item itemForUpdate;
        if (!itemRepository.existsById(itemId)) {
            log.error("Error объекта с id - {}, не существует", itemId);
            throw new NotFoundException(String.format("Error объекта с id - " + itemId + ", не существует"));
        } else {
            itemForUpdate = itemRepository.findById(itemId).get();
        }
        if (!userRepository.existsById(userId)) {
            log.error("Error такого пользователя не существует");
            throw new NotFoundException("Error такого пользователя не существует");
        }
        if (!itemRepository.existsItemByOwnerId(itemId, userId)) {
            log.debug("Debug пользователь с id - {}, не владеет объектом - {}", userId, itemId);
            throw new NotFoundException(String.format("Пользователь с id - %d, не владеет объектом - %d", userId, itemId));
        }

        if (itemRequestDto.getDescription() != null) {
            itemForUpdate.setDescription(itemRequestDto.getDescription());
        }
        if (itemRequestDto.getName() != null) {
            itemForUpdate.setName(itemRequestDto.getName());
        }
        if (itemRequestDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemRequestDto.getAvailable());
        }
        return itemRepository.save(itemForUpdate);
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("Error такого пользователя не существует");
            throw new NotFoundException("Error такого пользователя не существует");
        }
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public Item getById(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.error("Error объекта с id - {}, не существует", itemId);
            throw new NotFoundException(String.format("Error объекта с id - " + itemId + ", не существует"));
        }
        return itemRepository.findById(itemId).get();
    }


    @Override
    public List<Item> search(Optional<String> text, Long userId) {
        if (text.isEmpty() || text.get().isBlank()) {
            return new ArrayList<>();
        }
        String searchText = text.get().toLowerCase();
        return itemRepository.findItemByText(searchText);
    }

    @Override
    @Transactional
    public Comment createComment(CommentRequestDto commentRequestDto, Long userId, Long itemId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        if (bookingRepository.findPastBooking(userId, itemId, timestamp) == null) {
            log.error("Пользователь с id - " + userId + ", не брал в аренду предмет с id " + itemId);
            throw new ValidationException("Пользователь с id - " + userId + ", не брал в аренду предмет с id " + itemId);
        }
        if (userRepository.findById(userId).isEmpty()) {
            log.error("Пользователя с id " + userId + ", нет");
            throw new NotFoundException("Пользователя с id " + userId + ", нет");
        }
        Comment comment = new Comment();
        comment.setItem(getById(itemId));
        comment.setText(commentRequestDto.getText());
        comment.setAuthor(userRepository.findById(userId).get());
        return commentRepository.save(comment);
    }
}