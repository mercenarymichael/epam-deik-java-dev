package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Room;
import com.epam.training.ticketservice.repository.RoomRepository;
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

class RoomServiceImplTest {
    private final RoomRepository repository = mock(RoomRepository.class);
    private final RoomServiceImpl service = new RoomServiceImpl(repository);
    private Room room;

    @BeforeEach
    void setUp() {
        room = Room.builder()
                .name("Pedersoli")
                .seatRowCount(20)
                .seatColumnCount(10)
                .screenings(new ArrayList<>())
                .build();
    }

    @Test
    void testCreateRoomWhenRoomNotExist() {
        when(repository.findRoomByName(room.getName())).thenReturn(Optional.empty());
        assertTrue(service.createRoom(room.getName(), room.getSeatRowCount(), room.getSeatColumnCount()));
        verify(repository, times(1)).save(any(Room.class));
    }

    @Test
    void testCreateRoomWhenRoomExist() {
        when(repository.findRoomByName(room.getName())).thenReturn(Optional.of(room));
        assertFalse(service.createRoom(room.getName(), room.getSeatRowCount(), room.getSeatColumnCount()));
        verify(repository, never()).save(any(Room.class));
    }

    @Test
    void testDeleteRoomWhenRoomExist() {
        when(repository.findRoomByName(room.getName())).thenReturn(Optional.of(room));
        service.deleteRoom(room.getName());
        verify(repository, times(1)).delete(room);
    }

    @Test
    void testDeleteRoomWhenRoomNotExist() {
        when(repository.findRoomByName(room.getName())).thenReturn(Optional.empty());
        service.deleteRoom(room.getName());
        verify(repository, never()).delete(room);
    }

    @Test
    void testUpdateRoomWhenRoomExist() {
        when(repository.findRoomByName(room.getName())).thenReturn(Optional.of(room));
        assertTrue(service.updateRoom(room.getName(), room.getSeatRowCount(), room.getSeatColumnCount()));
        verify(repository, times(1)).save(any(Room.class));
    }

    @Test
    void testUpdateRoomWhenRoomNotExist() {
        when(repository.findRoomByName(room.getName())).thenReturn(Optional.empty());
        assertFalse(service.updateRoom(room.getName(), room.getSeatRowCount(), room.getSeatColumnCount()));
        verify(repository, never()).save(any(Room.class));
    }


    @Test
    void testGetAllRoomsWhenRoomExist() {
        when(repository.findAll()).thenReturn(List.of(room));
        assertEquals(Optional.of(List.of(room)), service.getAllRooms());
    }

    @Test
    void testGetAllRoomsWhenRoomNotExist() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(Optional.empty(), service.getAllRooms());
    }
}