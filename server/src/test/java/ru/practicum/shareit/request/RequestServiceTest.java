package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestData;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void create_whenUserFound_thenReturnItemRequest() {
        Long userId = 1L;
        ItemRequestData itemRequestData = new ItemRequestData();
        itemRequestData.setDescription("description");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("description");
        itemRequest.setOwner(new User());

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        Mockito.when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequest result = itemRequestService.create(userId, itemRequestData);

        assertNotNull(result);
        assertEquals("description", result.getDescription());
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void create_whenUserNotFound_thenThrowNotFoundException() {
        Long userId = 1L;
        ItemRequestData itemRequestData = new ItemRequestData();
        itemRequestData.setDescription("description");

        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemRequestService.create(userId, itemRequestData);
        });

        assertEquals("Error такого пользователя не существует", thrown.getMessage());
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository, Mockito.never()).findById(userId);
        Mockito.verify(itemRequestRepository, Mockito.never()).save(any(ItemRequest.class));
    }

    @Test
    void getAllByUser_whenUserFound_thenReturnItemRequests() {
        Long userId = 1L;
        ItemRequest itemRequest1 = new ItemRequest();
        ItemRequest itemRequest2 = new ItemRequest();
        List<ItemRequest> itemRequests = List.of(itemRequest1, itemRequest2);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(itemRequestRepository.findByOwnerId(userId)).thenReturn(itemRequests);

        List<ItemRequest> result = itemRequestService.getAllByUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(itemRequestRepository).findByOwnerId(userId);
    }

    @Test
    void getAllByUser_whenUserNotFound_thenThrowNotFoundException() {
        Long userId = 1L;

        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getAllByUser(userId);
        });

        assertEquals("Error такого пользователя не существует", thrown.getMessage());
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(itemRequestRepository, Mockito.never()).findByOwnerId(userId);
    }

    @Test
    void getAllByOtherUsers_whenUserFound_thenReturnItemRequests() {
        Long userId = 1L;
        ItemRequest itemRequest1 = new ItemRequest();
        ItemRequest itemRequest2 = new ItemRequest();
        List<ItemRequest> itemRequests = List.of(itemRequest1, itemRequest2);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(itemRequestRepository.findAllByOthersOwners(userId)).thenReturn(itemRequests);

        List<ItemRequest> result = itemRequestService.getAllByOtherUsers(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(itemRequestRepository).findAllByOthersOwners(userId);
    }

    @Test
    void getAllByOtherUsers_whenUserNotFound_thenThrowNotFoundException() {
        Long userId = 1L;

        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getAllByOtherUsers(userId);
        });

        assertEquals("Error такого пользователя не существует", thrown.getMessage());
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(itemRequestRepository, Mockito.never()).findAllByOthersOwners(userId);
    }

    @Test
    void getById_whenRequestExists_thenReturnItemRequest() {
        Long requestId = 1L;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);

        Mockito.when(itemRequestRepository.existsById(requestId)).thenReturn(true);
        Mockito.when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        ItemRequest result = itemRequestService.getById(requestId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        Mockito.verify(itemRequestRepository).existsById(requestId);
        Mockito.verify(itemRequestRepository).findById(requestId);
    }

    @Test
    void getById_whenRequestNotExists_thenThrowNotFoundException() {
        Long requestId = 1L;

        Mockito.when(itemRequestRepository.existsById(requestId)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getById(requestId);
        });

        assertEquals("Error запрос не найден", thrown.getMessage());
        Mockito.verify(itemRequestRepository).existsById(requestId);
        Mockito.verify(itemRequestRepository, Mockito.never()).findById(requestId);
    }
}
