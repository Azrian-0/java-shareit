package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Обработан POST /booking запрос. Пользователем с ID {}", userId);
        if (bookingRequestDto.getItemId() == null) {
            throw new ValidationException("Не указан id предмета");
        }
        if (bookingRequestDto.getStart() == null || bookingRequestDto.getEnd() == null) {
            throw new ValidationException("Дата не может быть пустой.");
        }
        LocalDateTime startTime = LocalDateTime.parse(bookingRequestDto.getStart());
        LocalDateTime endTime = LocalDateTime.parse(bookingRequestDto.getEnd());
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime) || endTime.isBefore(LocalDateTime.now().withNano(0)) || startTime.isBefore(LocalDateTime.now().withNano(0))) {
            throw new ValidationException("Дата начала не может быть позже даты окончания");
        }
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
