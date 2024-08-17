package ru.practicum.shareit.service.booking;

import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking create(BookingRequestDto bookingRequest, Long userId);

    Booking setApproved(Optional<Boolean> isApproved, Long bookingId, Long userId);

    Booking getById(Long bookingId, Long userId);

    List<Booking> getBookingsByRequester(Long userId, Optional<String> status);

    List<Booking> getBookingsByOwner(Long userId, Optional<String> status);
}
