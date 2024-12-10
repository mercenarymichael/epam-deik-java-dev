package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.entity.Movie;

import java.util.List;
import java.util.Optional;


public interface MovieService {
    boolean createMovie(String title, String genre, Integer runtime);

    boolean updateMovie(String title, String genre, Integer runtime);

    void deleteMovie(String title);

    Optional<List<Movie>> getAllMovies();
}
