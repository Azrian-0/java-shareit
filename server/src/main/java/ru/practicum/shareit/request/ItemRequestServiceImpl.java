package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestData;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequest create(Long userId, ItemRequestData itemRequestData) {
        if (!userRepository.existsById(userId)) {
            log.error("Error такого пользователя не существует");
            throw new NotFoundException("Error такого пользователя не существует");
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestData.getDescription());
        itemRequest.setOwner(userRepository.findById(userId).get());
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getAllByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("Error такого пользователя не существует");
            throw new NotFoundException("Error такого пользователя не существует");
        }
        return itemRequestRepository.findByOwnerId(userId);
    }

    @Override
    public List<ItemRequest> getAllByOtherUsers(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("Error такого пользователя не существует");
            throw new NotFoundException("Error такого пользователя не существует");
        }
        return itemRequestRepository.findAllByOthersOwners(userId);
    }

    @Override
    public ItemRequest getById(Long itemRequestId) {
        if (!itemRequestRepository.existsById(itemRequestId)) {
            log.error("Error запрос не найден");
            throw new NotFoundException("Error запрос не найден");
        }
        return itemRequestRepository.findById(itemRequestId).get();
    }
}
