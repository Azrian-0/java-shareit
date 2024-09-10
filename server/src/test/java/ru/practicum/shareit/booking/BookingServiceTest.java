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
    private BookingServiceImpl bookingServiceImpl;

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

        Booking request = bookingServiceImpl.create(bookingRequestDto, userId);

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

        Assertions.assertThrows(ValidationException.class, () -> bookingServiceImpl.create(bookingRequestDto, userId));
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

        bookingServiceImpl.setApproved(Optional.of(true), bookingId, userId);

        Mockito.verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking bookingSave = bookingArgumentCaptor.getValue();

        Assertions.assertEquals(BookingStatus.APPROVED, bookingSave.getStatus());
    }

    @Test
    protected void setApproved_whenBookingDoesNotExist_thenReturnConflictException() {
        long bookingId = 1L;
        long userId = 1L;

        Mockito.when(bookingRepository.existsById(bookingId)).thenReturn(false);

        Assertions.assertThrows(ConflictException.class, () -> bookingServiceImpl.setApproved(Optional.of(true), bookingId, userId));
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

        Booking request = bookingServiceImpl.getById(bookingId, userId);

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

        Assertions.assertThrows(ConflictException.class, () -> bookingServiceImpl.getById(bookingId, userId));
    }

    @Test
    protected void getBookingsByRequester_whenStatusAll_thenReturnBookings() {
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(1L);
        List<Booking> bookings = List.of(booking);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(bookingRepository.findByBookerId(userId)).thenReturn(bookings);

        List<Booking> result = bookingServiceImpl.getBookingsByRequester(userId, Optional.of("all"));

        Assertions.assertEquals(bookings, result);
    }

    @Test
    protected void getBookingsByOwner_whenInvalidStatus_thenReturnNotFoundException() {
        long userId = 1L;

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        Assertions.assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBookingsByOwner(userId, Optional.of("invalidStatus")));
    }
}
