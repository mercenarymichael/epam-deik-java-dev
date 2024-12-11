package com.epam.training.ticketservice.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    private Movie movie;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .id(1)
                .title("Sátántangó")
                .genre("drama")
                .runtime(450)
                .screenings(new ArrayList<>())
                .build();
        movie2 = Movie.builder()
                .id(1)
                .title("Sátántangó")
                .genre("drama")
                .runtime(450)
                .screenings(new ArrayList<>())
                .build();
    }

    @Test
    void testEquals() {
        assertEquals(movie, movie2);
        movie2.setGenre("sci-fi");
        assertNotEquals(movie2, movie);

        assertNotEquals("Alma", movie);
        assertNotEquals(null, movie);
    }

    @Test
    void testHashCode() {
        assertEquals(movie.hashCode(), movie2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(movie.toString(), movie2.toString());
        assertEquals("Sátántangó (drama, 450 minutes)", movie.toString());
    }
}