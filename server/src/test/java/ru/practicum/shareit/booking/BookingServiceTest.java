package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemServiceImpl itemService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @Test
    protected void createRequest_whenValidationIsCorrect_thenReturnBooking() {
        Long userId = 1L;
        Long itemId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemId);
        bookingRequestDto.setStart(LocalDateTime.now().toString());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1).toString());
        User user = new User();
        user.setId(2L);
        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);

        Mockito.when(itemService.getById(bookingRequestDto.getItemId())).thenReturn(item);
        Mockito.when(userService.findById(userId)).thenReturn(user);

        Booking request = bookingService.create(bookingRequestDto, userId);

        Assertions.assertEquals(bookingRequestDto.getItemId(), request.getItem().getId());
    }

    @Test
    protected void createRequest_whenItemUnavailable_thenReturnValidationException() {
        long userId = 1L;
        long itemId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemId);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1).toString());
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2).toString());

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(new User());
        item.getOwner().setId(userId);
        item.setAvailable(false);

        Mockito.when(itemService.getById(itemId)).thenReturn(item);

        Assertions.assertThrows(ValidationException.class, () -> bookingService.create(bookingRequestDto, userId));
    }

    @Test
    protected void setApproved_whenBookingExistsAndOwner_thenUpdateBookingStatus() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(userId);
        booking.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.existsById(bookingId)).thenReturn(true);

        bookingService.setApproved(Optional.of(true), bookingId, userId);

        Mockito.verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking bookingSave = bookingArgumentCaptor.getValue();

        Assertions.assertEquals(BookingStatus.APPROVED, bookingSave.getStatus());
    }

    @Test
    protected void setApproved_whenBookingDoesNotExist_thenReturnConflictException() {
        long bookingId = 1L;
        long userId = 1L;

        Mockito.when(bookingRepository.existsById(bookingId)).thenReturn(false);

        Assertions.assertThrows(ConflictException.class, () -> bookingService.setApproved(Optional.of(true), bookingId, userId));
    }

    @Test
    protected void getBookingById_whenBookingExists_thenReturnBooking() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(new User());
        booking.getBooker().setId(userId);

        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.existsById(bookingId)).thenReturn(true);

        Booking request = bookingService.getById(bookingId, userId);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(bookingId, request.getId());
    }

    @Test
    protected void getBookingById_whenBookingNotFound_thenReturnConflictException() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(new User());
        booking.getBooker().setId(2L);

        Mockito.when(bookingRepository.existsById(bookingId)).thenReturn(false);

        Assertions.assertThrows(ConflictException.class, () -> bookingService.getById(bookingId, userId));
    }

    @Test
    protected void getBookingsByRequester_whenStatusAll_thenReturnBookings() {
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(1L);
        List<Booking> bookings = List.of(booking);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(bookingRepository.findByBookerId(userId)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(userId, Optional.of("all"));

        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_All() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findByBookerId(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(1L, Optional.of("all"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_Current() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findCurrentBookings(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(1L, Optional.of("current"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_Past() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findPastBookings(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(1L, Optional.of("past"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_Future() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findFutureBookings(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(1L, Optional.of("future"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_Waiting() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findBookingsByStatusByUserId(1L, "WAITING")).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(1L, Optional.of("waiting"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_Rejected() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findBookingsByStatusByUserId(1L, "REJECTED")).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRequester(1L, Optional.of("rejected"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByRequester_UserNotFound() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingsByRequester(1L, Optional.of("all")));
    }

    @Test
    void getBookingsByRequester_InvalidStatus() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingsByRequester(1L, Optional.of("invalid_status")));
    }

    @Test
    void getBookingsByOwner_All() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findByOwnerId(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByOwner(1L, Optional.of("all"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByOwner_Current() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findOwnerCurrentBookings(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByOwner(1L, Optional.of("current"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByOwner_Past() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findOwnerPastBookings(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByOwner(1L, Optional.of("past"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByOwner_Future() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findOwnerFutureBookings(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByOwner(1L, Optional.of("future"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByOwner_Waiting() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findOwnerBookingsByStatusByUserId(1L, "WAITING")).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByOwner(1L, Optional.of("waiting"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByOwner_Rejected() {
        User user = new User();
        user.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findOwnerBookingsByStatusByUserId(1L, "REJECTED")).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByOwner(1L, Optional.of("rejected"));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookings, result);
    }

    @Test
    void getBookingsByOwnerUser_NotFound() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwner(1L, Optional.of("all")));
    }

    @Test
    void getBookingsByOwner_InvalidStatus() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwner(1L, Optional.of("invalid_status")));
    }
}
