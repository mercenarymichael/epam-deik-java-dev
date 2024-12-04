package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}