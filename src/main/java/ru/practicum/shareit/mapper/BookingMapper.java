package ru.practicum.shareit.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.ItemForBookingDto;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto map(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(mapToItemForBooking(booking.getItem()))
                .booker(UserMapper.map(booking.getBooker()))
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    private static ItemForBookingDto mapToItemForBooking(Item item) {
        return ItemForBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
