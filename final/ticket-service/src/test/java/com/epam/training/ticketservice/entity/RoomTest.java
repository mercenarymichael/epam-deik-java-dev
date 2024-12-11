package com.epam.training.ticketservice.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    private Room room;
    private Room room2;

    @BeforeEach
    void setUp() {
        room = Room.builder()
                .id(1)
                .name("Pedersoli")
                .seatRowCount(20)
                .seatColumnCount(10)
                .screenings(new ArrayList<>())
                .build();
        room2 = Room.builder()
                .id(1)
                .name("Pedersoli")
                .seatRowCount(20)
                .seatColumnCount(10)
                .screenings(new ArrayList<>())
                .build();
    }

    @Test
    void testEquals() {
        assertEquals(room, room2);
        room2.setName("Nagyterem");
        assertNotEquals(room2, room);

        assertNotEquals("Alma", room);
        assertNotEquals(null, room);
    }

    @Test
    void testHashCode() {
        assertEquals(room.hashCode(), room2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(room.toString(), room2.toString());
        assertEquals("Room Pedersoli with 200 seats, 20 rows and 10 columns", room.toString());
    }
}