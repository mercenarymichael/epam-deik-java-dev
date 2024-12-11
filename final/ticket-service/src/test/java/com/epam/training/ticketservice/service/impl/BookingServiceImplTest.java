package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.BookedSeat;
import com.epam.training.ticketservice.entity.Booking;
import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.entity.Role;
import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.entity.Screening;
import com.epam.training.ticketservice.exception.BookingException;
import com.epam.training.ticketservice.exception.ScreeningException;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final ScreeningService screeningService = mock(ScreeningService.class);
    private final AccountService accountService = mock(AccountService.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final BookingService bookingService =
            new BookingServiceImpl(bookingRepository, screeningService, accountService, accountRepository);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Movie movie;
    private Screening screening;
    private Room room;
    private Account account;
    private Booking booking;
    private BookedSeat seat;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .title("Sátántangó")
                .genre("drama")
                .runtime(450)
                .screenings(new ArrayList<>())
                .build();
        room = Room.builder()
                .name("Pedersoli")
                .seatRowCount(20)
                .seatColumnCount(10)
                .screenings(new ArrayList<>())
                .build();
        screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startTime("2021-03-15 08:00"))
                .build();
        account = Account.builder()
                .username("sanyi")
                .password("asd123")
                .role(Role.USER)
                .bookings(new ArrayList<>())
                .build();
        seat = BookedSeat
                .builder()
                .seatRow(5)
                .seatCol(5)
                .build();
        booking = Booking.builder()
                .bookedSeats(List.of(seat))
                .build();

    }

    LocalDateTime startTime(String start) {
        return LocalDateTime.parse(start, formatter);
    }

    @Test
    void testBookWhenSeatExistsAndSeatNotTaken() {
        when(screeningService.findScreening(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00")))
            .thenReturn(screening);
        when(accountService.getCurrentAccount()).thenReturn(Optional.of(account));
        when(bookingRepository.findBookingByScreening(screening)).thenReturn(new ArrayList<>());
        assertEquals("Seats booked: (5,5); the price for this booking is 1500 HUF",
                bookingService.book(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00"), "5,5"));
    }

    @Test
    void testBookWhenSeatExistsAndSeatTaken() {
        when(screeningService.findScreening(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00")))
                .thenReturn(screening);
        when(accountService.getCurrentAccount()).thenReturn(Optional.of(account));
        when(bookingRepository.findBookingByScreening(screening)).thenReturn(List.of(booking));
        BookingException exception = assertThrows(BookingException.class, () ->
                bookingService.book(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00"), "5,5")
        );
        assertEquals("Seat (5,5) is already taken", exception.getMessage());
    }

    @Test
    void testBookWhenSeatNotExist() {
        when(screeningService.findScreening(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00")))
                .thenReturn(screening);
        when(accountService.getCurrentAccount()).thenReturn(Optional.of(account));
        when(bookingRepository.findBookingByScreening(screening)).thenReturn(List.of(booking));
        BookingException exception = assertThrows(BookingException.class, () ->
                bookingService.book(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00"), "30,30")
        );
        assertEquals("Seat (30,30) does not exist in this room", exception.getMessage());
        exception = assertThrows(BookingException.class, () ->
                bookingService.book(movie.getTitle(), room.getName(), startTime("2021-03-15 08:00"), "2,30")
        );
        assertEquals("Seat (2,30) does not exist in this room", exception.getMessage());
    }

    @Test
    void testIsSeatTaken() {

    }
}