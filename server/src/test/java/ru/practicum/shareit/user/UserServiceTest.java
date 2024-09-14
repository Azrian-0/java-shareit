package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    protected void findUserById_whenUserFound_thenReturnUser() {
        long userId = 1L;
        User expectedUser = new User();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        User user = userService.findById(userId);

        Assertions.assertEquals(expectedUser, user);
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(userRepository).existsById(userId);
    }

    @Test
    protected void findUserById_whenUserNotFound_thenReturnNotFoundException() {
        long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> userService.findById(userId));
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository, Mockito.never()).findById(anyLong());
    }

    @Test
    protected void update_whenUserFound_thenReturnUser() {
        long userId = 1L;
        User oldUser = new User();
        oldUser.setName("name");
        oldUser.setEmail("email@email.ru");

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("name1");
        userRequestDto.setEmail("email1@email.ru");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        userService.update(userRequestDto, userId);

        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        Assertions.assertEquals("name1", savedUser.getName());
        Assertions.assertEquals("email1@email.ru", savedUser.getEmail());
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(userRepository).existsById(userId);
    }

    @Test
    protected void update_whenUserNotFound_thenReturnNotFoundException() {
        long userId = 1L;
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("name");
        userRequestDto.setEmail("email@email.ru");
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> userService.update(userRequestDto, userId));
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    protected void deleteUserById_whenUserFound_thenReturnCorrectMessage() {
        long userId = 1L;
        String expectedMessage = "Пользователь с id - " + userId + ", удалил свой аккаунт";
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        String message = userService.deleteById(userId);

        Assertions.assertEquals(expectedMessage, message);
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    protected void deleteUserById_whenUserNotFound_thenReturnCorrectMessage() {
        long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> userService.deleteById(userId));
        Mockito.verify(userRepository).existsById(userId);
        Mockito.verify(userRepository, Mockito.never()).deleteById(userId);
    }

    @Test
    protected void findAllUsers_thenVerify() {
        userService.findAll();

        Mockito.verify(userRepository).findAll();
    }

    @Test
    protected void createUser_thenVerify() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("name");
        userRequestDto.setEmail("email@email.ru");

        userService.create(userRequestDto);

        Mockito.verify(userRepository).save(any(User.class));
    }
}
