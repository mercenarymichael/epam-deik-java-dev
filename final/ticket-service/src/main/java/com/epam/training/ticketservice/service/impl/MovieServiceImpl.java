package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final AccountService accountService;

    @Override
    public boolean createMovie(String title, String genre, Integer runtime) {
        Optional<Movie> present = movieRepository.findMovieByTitle(title);
        if (present.isEmpty()) {
            Movie movie = Movie.builder()
                    .title(title)
                    .genre(genre)
                    .runtime(runtime)
                    .build();
            movieRepository.save(movie);
            return true;
        }
        return false;
    }


    @Override
    public boolean updateMovie(String title, String genre, Integer runtime) {
        Optional<Movie> movie = movieRepository.findMovieByTitle(title);
        if (movie.isPresent()) {
            movie.get().setGenre(genre);
            movie.get().setRuntime(runtime);
            movieRepository.save(movie.get());
            return true;
        }
        return false;
    }


    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.findMovieByTitle(title);
        movie.ifPresent(movieRepository::delete);
    }


    @Override
    public Optional<List<Movie>> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.isEmpty() ? Optional.empty() : Optional.of(movies);
    }
}
