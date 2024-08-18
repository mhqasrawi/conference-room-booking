package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.RoomsNotFoundException;
import com.mashreq.room.conference.booking.query.FindAllRoomsQuery;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FindAllRoomsHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private FindAllRoomsHandler findAllRoomsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidQuery_whenHandle_thenReturnsRoomDTOList() {
        Room room1 = new Room();
        room1.setName("Room 1");
        room1.setCapacity(10);
        Room room2 = new Room();
        room2.setName("Room 2");
        room2.setCapacity(20);
        List<Room> roomList = Arrays.asList(room1, room2);

        when(roomRepository.findAll()).thenReturn(roomList);

        FindAllRoomsQuery query = new FindAllRoomsQuery();
        List<RoomDTO> result = findAllRoomsHandler.handle(query);

        assertEquals(2, result.size());
        assertEquals("Room 1", result.get(0).getName());
        assertEquals(10, result.get(0).getCapacity());
        assertEquals("Room 2", result.get(1).getName());
        assertEquals(20, result.get(1).getCapacity());
    }

    @Test
    void givenEmptyRepository_whenHandle_thenThrowsRoomsNotFoundException() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList());

        FindAllRoomsQuery query = new FindAllRoomsQuery();

        RoomsNotFoundException exception = assertThrows(RoomsNotFoundException.class, () -> {
            findAllRoomsHandler.handle(query);
        });

        assertEquals(BookingError.ROOM_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }
}