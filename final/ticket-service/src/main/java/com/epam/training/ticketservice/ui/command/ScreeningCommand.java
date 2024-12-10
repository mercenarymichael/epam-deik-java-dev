package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.exception.ScreeningException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.ScreeningService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@ShellComponent
public class ScreeningCommand {
    private final ScreeningService screeningService;
    private final AccountService accountService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScreeningCommand(ScreeningService screeningService, AccountService accountService) {
        this.screeningService = screeningService;
        this.accountService = accountService;
    }

    @ShellMethod(key = "create screening", value = "Creates new screening")
    @ShellMethodAvailability("isPrivileged")
    public String createScreening(String movieTitle, String roomName, String start) {
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
            screeningService.createScreening(movieTitle, roomName, startDateTime);
        } catch (ScreeningException e) {
            return e.getMessage();
        }
        return "Screening created";
    }

    @ShellMethod(key = "delete screening", value = "Delete specified screening")
    @ShellMethodAvailability("isPrivileged")
    public String deleteScreening(String movieTitle, String roomName, String start) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        screeningService.deleteScreening(movieTitle, roomName, startDateTime);
        return "Screening deleted";
    }

    @ShellMethod(key = "list screenings", value = "List all screenings")
    public String listScreenings() {
        return screeningService.getAllScreenings()
                .map(screenings -> screenings.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")))
                .orElse("There are no screenings");
    }

    private Availability isPrivileged() {
        return accountService.getIsPrivileged() ? Availability.available()
                :
                Availability.unavailable("Not admin or not logged in");
    }
}
