package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.entity.Screening;
import com.epam.training.ticketservice.exception.ScreeningException;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public void createScreening(String movieTitle, String roomName, LocalDateTime start) throws ScreeningException {
        Optional<Movie> movie = movieRepository.findMovieByTitle(movieTitle);
        Optional<Room> room = roomRepository.findRoomByName(roomName);
        List<Screening> savedScreenings = screeningRepository.findAll();

        if (movie.isPresent() && room.isPresent()) {
            LocalDateTime endDateTime = start.plusMinutes(movie.get().getRuntime());
            for (Screening screening : savedScreenings) {
                LocalDateTime currentStart = screening.getStartDateTime();
                LocalDateTime currentEnd = screening.getStartDateTime().plusMinutes(screening.getMovie().getRuntime());
                if (!(start.isAfter(currentEnd) || endDateTime.isBefore(currentStart))
                        && roomName.equals(screening.getRoom().getName())) {
                    throw new ScreeningException("There is an overlapping screening");
                } else if (start.isAfter(currentEnd.plusMinutes(-1)) && start.isBefore(endDateTime.plusMinutes(10))
                        && roomName.equals(screening.getRoom().getName())) {
                    throw new ScreeningException("This would start in the break period "
                            + "after another screening in this room");
                }
            }
            screeningRepository.save(Screening.builder()
                    .movie(movie.get())
                    .room(room.get())
                    .startDateTime(start)
                    .build()
            );
        }
    }


    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime start) {
        Room room = roomRepository.findRoomByName(roomName).orElse(null);
        Movie movie = movieRepository.findMovieByTitle(movieTitle).orElse(null);
        Optional<Screening> screening = screeningRepository.findByRoomAndMovieAndStartDateTime(room, movie, start);
        screening.ifPresent(screeningRepository::delete);
    }

    @Override
    public Optional<List<Screening>> getAllScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        return screenings.isEmpty() ? Optional.empty() : Optional.of(screenings);
    }
}
