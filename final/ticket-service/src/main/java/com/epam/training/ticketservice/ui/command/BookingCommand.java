package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.exception.BookingException;
import com.epam.training.ticketservice.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
@AllArgsConstructor
public class BookingCommand {
    private final BookingService bookingService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @ShellMethod(key = "book", value = "Book seats for a screening")
    public String book(String movieTitle, String roomName, String start, String seats) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, formatter);
            return bookingService.book(movieTitle, roomName, startTime, seats);
        } catch (BookingException e) {
            return e.getMessage();
        }
    }
}
