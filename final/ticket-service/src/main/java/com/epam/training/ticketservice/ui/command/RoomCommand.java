package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {
    private final AccountService accountService;
    private final RoomService roomService;

    @ShellMethod(key = "create room", value = "Add new room to database")
    @ShellMethodAvailability("isPrivileged")
    public String createRoom(String name, Integer seatRowCount, Integer seatColumnCount) {
        return roomService.createRoom(name, seatRowCount, seatColumnCount) ? "Room created!"
                :
                "Room already exists!";
    }

    @ShellMethod(key = "update room", value = "Update room specified by name")
    @ShellMethodAvailability("isPrivileged")
    public String updateRoom(String name, Integer seatRowCount, Integer seatColumnCount) {
        return roomService.updateRoom(name, seatRowCount, seatColumnCount) ? "Room updated!"
                :
                "Cannot find room in database!";
    }


    @ShellMethod(key = "delete room", value = "Delete room by name")
    @ShellMethodAvailability("isPrivileged")
    public String deleteRoom(String name) {
        roomService.deleteRoom(name);
        return "Room deleted!";
    }

    @ShellMethod(key = "list rooms", value = "List all rooms from database")
    public String listMovies() {
        return roomService.getAllRooms()
                .map(rooms -> rooms.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")))
                .orElse("There are no rooms at the moment");
    }

    private Availability isPrivileged() {
        return accountService.getIsPrivileged() ? Availability.available()
                :
                Availability.unavailable("Not admin or not logged in");
    }
}
