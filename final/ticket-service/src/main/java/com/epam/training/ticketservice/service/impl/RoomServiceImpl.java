package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public boolean createRoom(String name, Integer seatRowCount, Integer seatColumnCount) {
        Optional<Room> present = roomRepository.findRoomByName(name);
        if (present.isEmpty()) {
            Room room = Room.builder()
                    .name(name)
                    .seatRowCount(seatRowCount)
                    .seatColumnCount(seatColumnCount)
                    .build();
            roomRepository.save(room);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRoom(String name, Integer seatRowCount, Integer seatColumnCount) {
        Optional<Room> room = roomRepository.findRoomByName(name);
        if (room.isPresent()) {
            room.get().setSeatRowCount(seatRowCount);
            room.get().setSeatColumnCount(seatColumnCount);
            roomRepository.save(room.get());
            return true;
        }
        return false;
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.findRoomByName(name);
        room.ifPresent(roomRepository::delete);
    }

    @Override
    public Optional<List<Room>> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.isEmpty() ? Optional.empty() : Optional.of(rooms);
    }
}
