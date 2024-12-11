package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieServiceImplTest {
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final AccountService accountService = mock(AccountService.class);
    private final MovieService movieService = new MovieServiceImpl(movieRepository, accountService);
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .title("Sátántangó")
                .genre("drama")
                .runtime(450)
                .screenings(new ArrayList<>())
                .build();
    }

    @Test
    void testCreateMovieWhenMovieNotExists() {
        when(movieRepository.findMovieByTitle("Sátántangó"))
                .thenReturn(Optional.empty());
        assertTrue(movieService.createMovie("Sátántangó", "drama", 450));
    }

    @Test
    void testCreateMovieWhenMovieExists() {
        when(movieRepository.findMovieByTitle("Sátántangó"))
                .thenReturn(Optional.of(this.movie));

        assertFalse(movieService.createMovie("Sátántangó", "drama", 450));
    }

    @Test
    void testUpdateMovieWhenMovieNotExists() {
        when(movieRepository.findMovieByTitle("Sátántangó"))
                .thenReturn(Optional.empty());
        assertFalse(movieService.updateMovie("Sátántangó", "drama", 250));
    }

    @Test
    void testUpdateMovieWhenMovieExists() {
        when(movieRepository.findMovieByTitle("Sátántangó"))
                .thenReturn(Optional.of(this.movie));
        assertTrue(movieService.updateMovie("Sátántangó", "drama", 250));
    }

    @Test
    void testDeleteMovieWhenMovieExists() {
        when(movieRepository.findMovieByTitle("Sátántangó"))
                .thenReturn(Optional.of(this.movie));
        movieService.deleteMovie(movie.getTitle());
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    void testDeleteMovieWhenMovieNotExists() {
        when(movieRepository.findMovieByTitle("Sátántangó"))
                .thenReturn(Optional.empty());
        movieService.deleteMovie(movie.getTitle());
        verify(movieRepository, never()).delete(any(Movie.class));
    }

    @Test
    void testGetAllMoviesWhenMovieExists() {
        when(movieRepository.findAll())
                .thenReturn(List.of(movie));
        assertTrue(movieService.getAllMovies().isPresent());
    }

    @Test
    void testGetAllMoviesWhenMovieNotExists() {
        when(movieRepository.findAll())
                .thenReturn(new ArrayList<>());
        assertFalse(movieService.getAllMovies().isPresent());
    }
}