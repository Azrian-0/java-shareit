package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Обработан POST /booking запрос. Пользователем с ID {}", userId);
        return bookingClient.createRequest(bookingRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> answer(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @PathVariable Long bookingId, @RequestParam Optional<Boolean> approved) {
        log.info("Обработан PATCH /bookings/{}", userId);
        return bookingClient.setApproved(approved, bookingId, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @PathVariable Long bookingId) {
        log.info("Обработан GET /bookings/{}", bookingId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByRequesterId(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestParam Optional<String> status) {
        log.info("Обработан GET /bookings by Requester Id");
        return bookingClient.getBookingsByRequester(userId, status);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerId(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestParam Optional<String> status) {
        log.info("Обработан GET /bookings by Owner Id");
        return bookingClient.getBookingsByOwner(userId, status);
    }
}
