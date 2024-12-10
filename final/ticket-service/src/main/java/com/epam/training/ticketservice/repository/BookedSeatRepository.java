package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.BookedSeat;
import com.epam.training.ticketservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    Optional<BookedSeat> findBookedSeatByBookingAndSeatRowAndSeatCol(Booking booking, Integer seatRow, Integer seatCol);
}