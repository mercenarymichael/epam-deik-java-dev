package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    boolean createRoom(String name, Integer seatRowCount, Integer seatColumnCount);

    boolean updateRoom(String name, Integer seatRowCount, Integer seatColumnCount);

    void deleteRoom(String name);

    Optional<List<Room>> getAllRooms();
}
