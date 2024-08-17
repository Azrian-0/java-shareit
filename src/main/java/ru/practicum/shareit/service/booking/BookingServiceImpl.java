package ru.practicum.shareit.service.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.repository.booking.BookingRepository;
import ru.practicum.shareit.service.item.ItemService;
import ru.practicum.shareit.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public Booking create(BookingRequestDto bookingRequestDto, Long userId) {
        if (bookingRequestDto.getItemId() == null) {
            log.error("Не указан id предмета");
            throw new ValidationException("Не указан id предмета");
        }
        if (itemService.getById(bookingRequestDto.getItemId()).getOwner().getId().equals(userId)) {
            log.error("Пользователь не может запросить свой предмет");
            throw new ValidationException("Пользователь не может запросить свой предмет");
        }
        Booking booking = new Booking();
        booking.setItem(itemService.getById(bookingRequestDto.getItemId()));
        booking.setBooker(userService.findById(userId));
        booking.setStatus(BookingStatus.WAITING);
        if (bookingRequestDto.getStart() == null || bookingRequestDto.getEnd() == null) {
            log.error("Дата не может равняться null");
            throw new ValidationException("Дата не может быть пустой");
        }
        LocalDateTime startTime = bookingRequestDto.getStart();
        LocalDateTime endTime = bookingRequestDto.getEnd();
        if (!itemService.getById(bookingRequestDto.getItemId()).getAvailable()) {
            log.error("Объект с id - " + bookingRequestDto.getItemId() + ", не доступен");
            throw new ValidationException("Объект с id - " + bookingRequestDto.getItemId() + ", не доступен");
        }
        booking.setStart(startTime);
        booking.setEnd(endTime);
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime) || endTime.isBefore(LocalDateTime.now().withNano(0)) || startTime.isBefore(LocalDateTime.now().withNano(0))) {
            log.error("Дата начала должна быть до даты конца");
            throw new ValidationException("Дата начала должна быть до даты конца");
        }
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
                return bookingRepository.findBookingsByStatusByUserId(userId, statusLow.toUpperCase());
            }
            default -> {
                log.error("Статуса - " + status + ", не существует");
                throw new NotFoundException("Статуса - " + status + ", не существует");
            }
        }
    }

    @Override
    public List<Booking> getBookingsByOwner(Long userId, Optional<String> status) {
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
                return bookingRepository.findOwnerBookingsByStatusByUserId(userId, statusLow.toUpperCase());
            }
            default -> {
                log.error("Статуса - " + status + ", не существует");
                throw new NotFoundException("Статуса - " + status + ", не существует");
            }
        }
    }

}