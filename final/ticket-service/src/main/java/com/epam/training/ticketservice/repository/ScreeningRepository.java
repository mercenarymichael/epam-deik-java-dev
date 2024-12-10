package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    Optional<Screening> findByRoomAndMovieAndStartDateTime(Room room, Movie movie, LocalDateTime startDateTime);
}