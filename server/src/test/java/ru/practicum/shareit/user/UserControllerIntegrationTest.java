package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    protected void findUserById() {
        long userId = 0L;

        Mockito.when(userService.findById(userId)).thenReturn(new User());

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());
        Mockito.verify(userService).findById(userId);
    }

    @SneakyThrows
    @Test
    protected void create() {
        UserRequestDto userToCreate = new UserRequestDto();
        User user = new User();
        Mockito.when(userService.create(userToCreate)).thenReturn(user);

        String request = mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(objectMapper.writeValueAsString(user), request, true);
    }
}
