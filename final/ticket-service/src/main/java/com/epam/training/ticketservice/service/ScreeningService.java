package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.entity.Screening;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    void createScreening(String movieTitle, String roomName, LocalDateTime start);

    void deleteScreening(String movieTitle, String roomName, LocalDateTime start);

    Optional<List<Screening>> getAllScreenings();
}
