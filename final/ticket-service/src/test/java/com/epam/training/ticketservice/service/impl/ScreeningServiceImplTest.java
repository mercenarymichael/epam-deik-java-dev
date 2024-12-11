package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.entity.Screening;
import com.epam.training.ticketservice.exception.ScreeningException;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.ScreeningService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

class ScreeningServiceImplTest {
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final ScreeningService screeningService =
            new ScreeningServiceImpl(screeningRepository, movieRepository, roomRepository);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Movie movie;
    private Room room;
    private Screening screening;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .title("Sátántangó")
                .genre("drama")
                .runtime(450)
                .screenings(new ArrayList<>())
                .build();
        room = Room.builder()
                .name("Pedersoli")
                .seatRowCount(20)
                .seatColumnCount(10)
                .screenings(new ArrayList<>())
                .build();
        screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startTime("2021-03-15 08:00"))
                .build();
    }

    @Test
    void testCreateScreeningWhenMovieAndRoomExists() {
        when(movieRepository.findMovieByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));
        when(roomRepository.findRoomByName(room.getName()))
                .thenReturn(Optional.of(room));
        when(screeningRepository.findAll())
                .thenReturn(new ArrayList<>());

        screeningService.createScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45"));

        verify(screeningRepository, times(1)).save(any(Screening.class));
    }

    @Test
    void testCreateScreeningWhenRoomNotExistsAndMovieExists() {
        when(movieRepository.findMovieByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));
        when(roomRepository.findRoomByName(room.getName()))
                .thenReturn(Optional.empty());
        /*
        when(screeningRepository.findAll())
                .thenReturn(new ArrayList<>());

         */
        screeningService.createScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45"));

        verify(screeningRepository, never()).save(any(Screening.class));
    }

    @Test
    void testCreateScreeningWhenMovieNotExistsAndRoomExists() {
        when(movieRepository.findMovieByTitle(movie.getTitle()))
                .thenReturn(Optional.empty());
        when(roomRepository.findRoomByName(room.getName()))
                .thenReturn(Optional.of(room));
        /*
        when(screeningRepository.findAll())
                .thenReturn(new ArrayList<>());

         */
        screeningService.createScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45"));

        verify(screeningRepository, never()).save(any(Screening.class));
    }

    @Test
    void testCreateScreeningWhenMovieOverlapping() {
        when(movieRepository.findMovieByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));
        when(roomRepository.findRoomByName(room.getName()))
                .thenReturn(Optional.of(room));
        when(screeningRepository.findAll())
                .thenReturn(List.of(screening));



        ScreeningException exception = assertThrows(ScreeningException.class, () ->
                screeningService.createScreening(movie.getTitle(),
                        room.getName(),
                        startTime("2021-03-15 10:45"))
        );

        assertEquals("There is an overlapping screening", exception.getMessage());
    }


    @Test
    void testCreateScreeningWhenMovieOverlappingWithBreakPeriod() {
        when(movieRepository.findMovieByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));
        when(roomRepository.findRoomByName(room.getName()))
                .thenReturn(Optional.of(room));
        when(screeningRepository.findAll())
                .thenReturn(List.of(screening));



        ScreeningException exception = assertThrows(ScreeningException.class, () ->
                screeningService.createScreening(movie.getTitle(),
                        room.getName(),
                        startTime("2021-03-15 15:35"))
        );

        assertEquals("This would start in the break "
                + "period after another screening in this room", exception.getMessage());
    }

    @Test
    void testDeleteScreeningWhenScreeningExists() {
        when(screeningService.findScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45")))
                .thenReturn(screening);
        screeningService.deleteScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45"));
        verify(screeningRepository, times(1)).delete(screening);
    }

    @Test
    void testDeleteScreeningWhenScreeningDoesNotExist() {
        when(screeningService.findScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45")))
                .thenReturn(null);
        screeningService.deleteScreening(movie.getTitle(),
                room.getName(),
                startTime("2021-03-15 10:45"));
        verify(screeningRepository, never()).delete(screening);
    }

    @Test
    void testGetAllScreeningsWhenScreeningExists() {
        when(screeningRepository.findAll())
                .thenReturn(List.of(screening));
        assertEquals(Optional.of(List.of(screening)), screeningService.getAllScreenings());
    }

    @Test
    void testGetAllScreeningsWhenScreeningNotExists() {
        when(screeningRepository.findAll())
                .thenReturn(new ArrayList<>());
        assertTrue(screeningService.getAllScreenings().isEmpty());
    }



    LocalDateTime startTime(String start) {
        return LocalDateTime.parse(start, formatter);
    }
}