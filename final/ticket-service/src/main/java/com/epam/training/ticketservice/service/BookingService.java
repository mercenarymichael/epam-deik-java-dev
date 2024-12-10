package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.entity.Booking;
import java.time.LocalDateTime;

public interface BookingService {

    String book(String movieTitle, String roomName, LocalDateTime start, String seats);

    boolean isSeatTaken(int row, int col);
}
