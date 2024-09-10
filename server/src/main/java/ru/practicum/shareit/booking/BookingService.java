package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking create(BookingRequestDto bookingRequest, Long userId);

    Booking setApproved(Optional<Boolean> isApproved, Long bookingId, Long userId);

    Booking getById(Long bookingId, Long userId);

    List<Booking> getBookingsByRequester(Long userId, Optional<String> status);

    List<Booking> getBookingsByOwner(Long userId, Optional<String> status);
}
