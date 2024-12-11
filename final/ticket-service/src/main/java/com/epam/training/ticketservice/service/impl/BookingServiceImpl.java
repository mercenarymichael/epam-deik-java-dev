package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.BookedSeat;
import com.epam.training.ticketservice.entity.Booking;
import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.entity.Screening;
import com.epam.training.ticketservice.exception.BookingException;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.BookedSeatRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ScreeningService screeningService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private List<Pair<Integer, Integer>> seatPairs;
    private List<Booking> bookings;
    private boolean isBookingCompleted = true;
    private int ticketPrice = 1500;
    List<BookedSeat> bookedSeats;

    @Override
    @Transactional
    public String book(String movieTitle, String roomName, LocalDateTime start, String seats) throws BookingException {

        Screening screening = screeningService.findScreening(movieTitle, roomName, start);
        bookings = bookingRepository.findBookingByScreening(screening);
        Room room = screening.getRoom();

        bookedSeats = new ArrayList<>();

        processSeatsString(seats);
        for (Pair<Integer, Integer> seatPair : seatPairs) {
            if (isSeatNotExists(seatPair.getFirst(), seatPair.getSecond(), room)) {
                throw new BookingException("Seat (" + seatPair.getFirst() + ","
                        + seatPair.getSecond() + ") does not exist in this room");
            }

            if (isSeatTaken(seatPair.getFirst(), seatPair.getSecond())) {
                throw new BookingException("Seat (" + seatPair.getFirst() + ","
                        + seatPair.getSecond() + ") is already taken");
            }

            BookedSeat bookedSeat = BookedSeat.builder()
                    .seatRow(seatPair.getFirst())
                    .seatCol(seatPair.getSecond())
                    .build();
            bookedSeats.add(bookedSeat);
        }

        Account account = accountService.getCurrentAccount().get();
        Booking createBooking = Booking.builder()
                .screening(screening)
                .account(account)
                .bookedSeats(new ArrayList<>())
                .amount(bookedSeats.size() * ticketPrice)
                .build();

        for (BookedSeat bookedSeat : bookedSeats) {
            bookedSeat.setBooking(createBooking);
        }
        createBooking.getBookedSeats().addAll(bookedSeats);
        bookingRepository.save(createBooking);
        account.getBookings().add(createBooking);
        accountRepository.save(account);
        return printBooking(createBooking);
    }

    private boolean isSeatNotExists(int row, int col, Room room) {
        return row > room.getSeatRowCount() || col > room.getSeatColumnCount();
    }

    @Override
    public boolean isSeatTaken(int row, int col) {
        if (bookings.isEmpty()) {
            return false;
        }
        return bookings.stream()
                .anyMatch(booking -> booking.getBookedSeats().stream()
                        .anyMatch(seat -> seat.getSeatCol() == col && seat.getSeatCol() == row));
    }

    private void processSeatsString(String seats) throws BookingException {
        String[] seatsList = seats.split(" ");
        seatPairs = new ArrayList<>();
        try {
            for (String seat : seatsList) {
                int row = Integer.parseInt(seat.split(",")[0]);
                int col = Integer.parseInt(seat.split(",")[1]);
                seatPairs.add(Pair.of(row, col));
            }
        } catch (NumberFormatException e) {
            throw new BookingException("Invalid seats");
        }

    }

    private String printBooking(Booking booking) {
        StringBuilder sb = new StringBuilder();
        if (isBookingCompleted) {
            sb.append("Seats booked: ");

            sb.append(bookedSeats.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ")));
            sb.append("; the price for this booking is " + booking.getAmount() + " HUF");
        }
        return sb.toString();
    }
}
