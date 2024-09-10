package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    protected void findAllUsers_whenInvoked_thenReturnUserDtoCollectionInBody() {
        List<UserDto> expectedUsers = List.of(new UserDto());
        Mockito.when(userService.findAll()).thenReturn(List.of(new User()));

        List<UserDto> users = userController.findAll();

        Assertions.assertEquals(expectedUsers, users);
    }

    @Test
    protected void findUserById_whenInvoked_thenReturnUserDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@email.ru");
        user.setName("name");
        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);

        UserDto userDto = userController.findUserById(1L);

        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getName(), userDto.getName());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    protected void deleteUserById_whenInvoked_thenReturnMessage() {
        String expectedMessage = "{\"message\":\"Пользователь с id - 1, удалил свой аккаунт\"}";
        Mockito.when(userService.deleteById(1L))
                .thenReturn("Пользователь с id - " + 1L + ", удалил свой аккаунт");

        String message = userController.deleteById(1L);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    protected void create_whenInvoked_thenReturnUserDtoWithSameFieldsAsInUserRequest() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("gmail@gmail.ru");
        userRequestDto.setName("name");
        User expectedUserFields = new User();
        expectedUserFields.setEmail("gmail@gmail.ru");
        expectedUserFields.setName("name");
        Mockito.when(userService.create(any(UserRequestDto.class))).thenReturn(expectedUserFields);

        UserDto response = userController.create(userRequestDto);

        Assertions.assertEquals(expectedUserFields.getEmail(), response.getEmail());
        Assertions.assertEquals(expectedUserFields.getName(), response.getName());
    }

    @Test
    protected void update_whenInvoked_thenReturnUserDtoWithSameFieldsAsInUserRequest() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("gmail@gmail.ru");
        userRequestDto.setName("name");
        User expectedUserFields = new User();
        expectedUserFields.setEmail("gmail@gmail.ru");
        expectedUserFields.setName("name");
        Mockito.when(userService.update(any(UserRequestDto.class), anyLong())).thenReturn(expectedUserFields);

        UserDto response = userController.update(userRequestDto, 1L);

        Assertions.assertEquals(expectedUserFields.getEmail(), response.getEmail());
        Assertions.assertEquals(expectedUserFields.getName(), response.getName());
    }
}
