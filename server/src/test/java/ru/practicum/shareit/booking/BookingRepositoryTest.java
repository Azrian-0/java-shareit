package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private Item item;
    private Booking bookingCurrent;
    private Booking bookingFuture;
    private Booking bookingPast;

    @BeforeEach
    public void setUp() {
        booker = new User();
        booker.setName("Test User");
        booker.setEmail("testuser@example.com");
        userRepository.save(booker);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(booker);
        itemRepository.save(item);

        bookingCurrent = new Booking();
        bookingCurrent.setBooker(booker);
        bookingCurrent.setItem(item);
        bookingCurrent.setStart(LocalDateTime.now().minusDays(1));
        bookingCurrent.setEnd(LocalDateTime.now().plusDays(2));
        bookingCurrent.setStatus(BookingStatus.WAITING);
        bookingRepository.save(bookingCurrent);

        bookingFuture = new Booking();
        bookingFuture.setBooker(booker);
        bookingFuture.setItem(item);
        bookingFuture.setStart(LocalDateTime.now().plusDays(1));
        bookingFuture.setEnd(LocalDateTime.now().plusDays(2));
        bookingFuture.setStatus(BookingStatus.WAITING);
        bookingRepository.save(bookingFuture);

        bookingPast = new Booking();
        bookingPast.setBooker(booker);
        bookingPast.setItem(item);
        bookingPast.setStart(LocalDateTime.now().minusDays(3));
        bookingPast.setEnd(LocalDateTime.now().minusDays(2));
        bookingPast.setStatus(BookingStatus.WAITING);
        bookingRepository.save(bookingPast);
    }

    @AfterEach
    public void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindByBookerId() {
        List<Booking> bookings = bookingRepository.findByBookerId(booker.getId());
        Assertions.assertEquals(3, bookings.size());
    }

    @Test
    public void testFindPastBooking() {
        Booking pastBooking = bookingRepository.findPastBooking(
                booker.getId(), item.getId(), Timestamp.valueOf(LocalDateTime.now())
        );
        Assertions.assertNotNull(pastBooking);
        Assertions.assertEquals(bookingPast.getId(), pastBooking.getId());
    }

    @Test
    public void testFindCurrentBookings() {
        List<Booking> currentBookings = bookingRepository.findCurrentBookings(booker.getId());

        Assertions.assertEquals(currentBookings.size(), 1);
        Assertions.assertTrue(currentBookings.contains(bookingCurrent));
    }

    @Test
    public void testFindPastBookings() {
        List<Booking> pastBookings = bookingRepository.findPastBookings(booker.getId());
        Assertions.assertTrue(pastBookings.contains(bookingPast));
        Assertions.assertFalse(pastBookings.contains(bookingFuture));
    }

    @Test
    public void testFindFutureBookings() {
        List<Booking> futureBookings = bookingRepository.findFutureBookings(booker.getId());
        Assertions.assertTrue(futureBookings.contains(bookingFuture));
        Assertions.assertFalse(futureBookings.contains(bookingPast));
    }

    @Test
    public void testFindBookingsByStatusByUserId() {
        List<Booking> bookings = bookingRepository.findBookingsByStatusByUserId(booker.getId(), BookingStatus.WAITING);
        Assertions.assertTrue(bookings.contains(bookingFuture));
        Assertions.assertTrue(bookings.contains(bookingPast));
    }

    @Test
    public void testFindByOwnerId() {
        List<Booking> bookings = bookingRepository.findByOwnerId(booker.getId());
        Assertions.assertTrue(bookings.contains(bookingFuture));
        Assertions.assertTrue(bookings.contains(bookingPast));
    }

    @Test
    public void testFindOwnerCurrentBookings() {
        List<Booking> currentBookings = bookingRepository.findOwnerCurrentBookings(booker.getId());
        Assertions.assertTrue(currentBookings.contains(bookingCurrent));
    }

    @Test
    public void testFindOwnerPastBookings() {
        List<Booking> pastBookings = bookingRepository.findOwnerPastBookings(booker.getId());
        Assertions.assertTrue(pastBookings.contains(bookingPast));
        Assertions.assertFalse(pastBookings.contains(bookingFuture));
    }

    @Test
    public void testFindOwnerFutureBookings() {
        List<Booking> futureBookings = bookingRepository.findOwnerFutureBookings(booker.getId());
        Assertions.assertTrue(futureBookings.contains(bookingFuture));
        Assertions.assertFalse(futureBookings.contains(bookingPast));
    }

    @Test
    public void testFindOwnerBookingsByStatusByUserId() {
        List<Booking> bookings = bookingRepository.findOwnerBookingsByStatusByUserId(booker.getId(), BookingStatus.WAITING);
        Assertions.assertTrue(bookings.contains(bookingFuture));
        Assertions.assertTrue(bookings.contains(bookingPast));
    }
}
