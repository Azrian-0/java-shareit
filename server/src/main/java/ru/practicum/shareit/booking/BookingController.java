package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mappers.BookingMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Обработан POST /booking запрос. Пользователем с ID {}", userId);
        return BookingMapper.map(bookingService.create(bookingRequestDto, userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto answer(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @PathVariable Long bookingId, @RequestParam Optional<Boolean> approved) {
        log.info("Обработан PATCH /bookings/{}", userId);
        return BookingMapper.map(bookingService.setApproved(approved, bookingId, userId));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @PathVariable Long bookingId) {
        log.info("Обработан GET /bookings/{}", bookingId);
        return BookingMapper.map(bookingService.getById(bookingId, userId));
    }

    @GetMapping
    public List<BookingDto> getBookingsByRequesterId(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestParam Optional<String> status) {
        log.info("Обработан GET /bookings by Requester Id");
        return bookingService.getBookingsByRequester(userId, status).stream().map(BookingMapper::map).toList();
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestHeader("${shareIt.headers.userHeader}") Long userId, @RequestParam Optional<String> status) {
        log.info("Обработан GET /bookings by Owner Id");
        return bookingService.getBookingsByOwner(userId, status).stream().map(BookingMapper::map).toList();
    }
}
