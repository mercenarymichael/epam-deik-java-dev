package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {
    private final MovieService movieService;
    private final AccountService accountService;

    @ShellMethod(key = "create movie", value = "Add new movie to database")
    @ShellMethodAvailability("isPrivileged")
    public String createMovie(String title, String genre, Integer runtime) {
        return movieService.createMovie(title, genre, runtime) ? "Movie created!" : "Movie already exists!";
    }

    @ShellMethod(key = "update movie", value = "Update movie specified by title")
    @ShellMethodAvailability("isPrivileged")
    public String updateMovie(String title, String genre, Integer runtime) {
        return movieService.updateMovie(title, genre, runtime) ? "Movie updated!" : "Cannot find movie in database!";
    }

    @ShellMethod(key = "delete movie", value = "Delete movie from database")
    @ShellMethodAvailability("isPrivileged")
    public String deleteMovie(String title) {
        movieService.deleteMovie(title);
        return "Movie deleted!";
    }

    @ShellMethod(key = "list movies", value = "List all movies from database")
    public String listMovies() {
        return movieService.getAllMovies()
                .map(movies -> movies.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")))
                .orElse("There are no movies at the moment");
    }

    private Availability isPrivileged() {
        return accountService.getIsPrivileged() ? Availability.available()
                :
                Availability.unavailable("Not admin or not logged in");
    }
}
