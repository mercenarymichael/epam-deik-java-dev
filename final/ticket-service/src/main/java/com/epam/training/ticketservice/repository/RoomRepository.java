package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}