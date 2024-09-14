package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.ItemForBookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookingControllerIntegrationTest {
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
    protected void getBookingById() {
        long userId = 1L;
        long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(new Item());
        booking.setBooker(new User());
        booking.setStatus(BookingStatus.APPROVED);

        Mockito.when(bookingService.getById(bookingId, userId)).thenReturn(booking);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        Mockito.verify(bookingService).getById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    protected void create() {
        long userId = 1L;
        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(1L);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItem(new ItemForBookingDto());
        bookingDto.setBooker(new UserDto());
        Booking booking = new Booking();
        Item item = new Item();
        item.setAvailable(true);
        booking.setItem(item);
        booking.setBooker(new User());

        Mockito.when(bookingService.create(bookingRequest, userId)).thenReturn(booking);

        String request = mockMvc.perform(post("/bookings")
                .contentType("application/json")
                .header("X-Sharer-User-Id", userId)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(bookingDto), request);
    }
}
