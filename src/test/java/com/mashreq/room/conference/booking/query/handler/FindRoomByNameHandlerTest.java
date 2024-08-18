package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.RoomException;
import com.mashreq.room.conference.booking.query.FindRoomByNameQuery;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FindRoomByNameHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private FindRoomByNameHandler findRoomByNameHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidRoomName_whenHandle_thenReturnsRoomDTO() {
        String roomName = "Conference Room A";
        Room room = new Room();
        room.setName(roomName);
        room.setCapacity(50);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));

        FindRoomByNameQuery query = new FindRoomByNameQuery(roomName);
        RoomDTO result = findRoomByNameHandler.handle(query);

        assertEquals(roomName, result.getName());
        assertEquals(50, result.getCapacity());
    }

    @Test
    void givenNonExistentRoomName_whenHandle_thenThrowsRoomException() {
        String roomName = "Non Existent Room";

        when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());

        FindRoomByNameQuery query = new FindRoomByNameQuery(roomName);

        RoomException exception = assertThrows(RoomException.class, () -> {
            findRoomByNameHandler.handle(query);
        });

        assertEquals(BookingError.ROOM_NOT_FOUND.getCode(), exception.getCode().getCode());
    }
}