package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findRoomByName(String name);
}