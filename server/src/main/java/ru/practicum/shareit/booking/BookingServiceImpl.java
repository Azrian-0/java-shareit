package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Booking create(BookingRequestDto bookingRequestDto, Long userId) {
        if (itemService.getById(bookingRequestDto.getItemId()).getOwner().getId().equals(userId)) {
            log.error("Пользователь не может запросить свой предмет");
            throw new ValidationException("Пользователь не может запросить свой предмет");
        }
        Booking booking = new Booking();
        booking.setItem(itemService.getById(bookingRequestDto.getItemId()));
        booking.setBooker(userService.findById(userId));
        booking.setStatus(BookingStatus.WAITING);
        LocalDateTime startTime = LocalDateTime.parse(bookingRequestDto.getStart());
        LocalDateTime endTime = LocalDateTime.parse(bookingRequestDto.getEnd());
        if (!itemService.getById(bookingRequestDto.getItemId()).getAvailable()) {
            log.error("Объект с id - " + bookingRequestDto.getItemId() + ", не доступен");
            throw new ValidationException("Объект с id - " + bookingRequestDto.getItemId() + ", не доступен");
        }
        booking.setStart(startTime);
        booking.setEnd(endTime);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    @Transactional
    public Booking setApproved(Optional<Boolean> isApproved, Long bookingId, Long userId) {
        if (!bookingRepository.existsById(bookingId)) {
            log.error("Объекта с id - " + bookingId + ", не существует");
            throw new ConflictException("Объекта с id - " + bookingId + ", не существует");
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            log.error("Пользователь с id - " + userId + ", не владеет объектом с id - " + bookingId);
            throw new ValidationException("Вы не владеете этим предметом");
        }
        if (isApproved.get()) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(Long bookingId, Long userId) {
        if (!bookingRepository.existsById(bookingId)) {
            log.error("Объекта с id - " + bookingId + ", не существует");
            throw new ConflictException("Объекта с id - " + bookingId + ", не существует");
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (!(booking.getBooker().getId().longValue() == userId.longValue() || booking.getItem().getOwner().getId().longValue() == userId.longValue())) {
            log.error("Объект с id - " + bookingId + ", не может быть просмотрен пользователем с id - " + userId);
            throw new ConflictException("Вы не можете посмотреть данные об этом предмете");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByRequester(Long userId, Optional<String> status) {
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь с id - " + userId + " не найден.");
            throw new NotFoundException("Пользователь с id - " + userId + " не найден.");
        }
        String statusLow = status.map(String::toLowerCase).orElse("all");
        switch (statusLow) {
            case "all" -> {
                return bookingRepository.findByBookerId(userId);
            }
            case "current" -> {
                return bookingRepository.findCurrentBookings(userId);
            }
            case "past" -> {
                return bookingRepository.findPastBookings(userId);
            }
            case "future" -> {
                return bookingRepository.findFutureBookings(userId);
            }
            case "waiting", "rejected" -> {
                return bookingRepository.findBookingsByStatusByUserId(userId, BookingStatus.valueOf(statusLow.toUpperCase()));
            }
            default -> {
                log.error("Статуса - " + status + ", не существует");
                throw new NotFoundException("Статуса - " + status + ", не существует");
            }
        }
    }

    @Override
    public List<Booking> getBookingsByOwner(Long userId, Optional<String> status) {
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь с id - " + userId + " не найден.");
            throw new NotFoundException("Пользователь с id - " + userId + " не найден.");
        }
        String statusLow = status.get().toLowerCase();
        switch (statusLow) {
            case "all" -> {
                return bookingRepository.findByOwnerId(userId);
            }
            case "current" -> {
                return bookingRepository.findOwnerCurrentBookings(userId);
            }
            case "past" -> {
                return bookingRepository.findOwnerPastBookings(userId);
            }
            case "future" -> {
                return bookingRepository.findOwnerFutureBookings(userId);
            }
            case "waiting", "rejected" -> {
                return bookingRepository.findOwnerBookingsByStatusByUserId(userId, BookingStatus.valueOf(statusLow.toUpperCase()));
            }
            default -> {
                log.error("Статуса - " + status + ", не существует");
                throw new NotFoundException("Статуса - " + status + ", не существует");
            }
        }
    }
}
