package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.command.AddRoomCommand;
import com.mashreq.room.conference.booking.command.DeleteRoomCommand;
import com.mashreq.room.conference.booking.command.UpdateRoomCommand;
import com.mashreq.room.conference.booking.command.handler.*;
import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.exceptions.RoomException;
import com.mashreq.room.conference.booking.query.FindAllRoomsQuery;
import com.mashreq.room.conference.booking.query.FindRoomByNameQuery;
import com.mashreq.room.conference.booking.query.handler.FindAllRoomsHandler;
import com.mashreq.room.conference.booking.query.handler.FindRoomByNameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RoomControllerTest {

    @Mock
    private AddRoomHandler addRoomHandler;

    @Mock
    private UpdateRoomHandler updateRoomHandler;

    @Mock
    private DeleteRoomHandler deleteRoomHandler;

    @Mock
    private DisableRoomHandler disableRoomHandler;

    @Mock
    private EnableRoomHandler enableRoomHandler;

    @Mock
    private FindAllRoomsHandler findAllRoomsHandler;

    @Mock
    private FindRoomByNameHandler findRoomByNameHandler;

    @InjectMocks
    private RoomController roomController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidInput_whenAddRoom_thenCreatesNewRoom() throws URISyntaxException {
        when(addRoomHandler.handle(any(AddRoomCommand.class))).thenReturn("1");

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");

        ResponseEntity<Void> response = roomController.addRoom(roomDTO);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void givenValidInput_whenUpdateRoom_thenUpdatesRoom() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");

        ResponseEntity<Void> response = roomController.updateRoom("Conference Room A", roomDTO);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void givenValidName_whenDeleteRoom_thenDeletesRoom() {
        ResponseEntity<Void> response = roomController.deleteRoom("Conference Room A");

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void givenValidName_whenGetRoomByName_thenReturnsRoom() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");

        when(findRoomByNameHandler.handle(any(FindRoomByNameQuery.class))).thenReturn(roomDTO);

        RoomDTO response = roomController.getRoomByName("Conference Room A");

        assertEquals(roomDTO, response);
    }

    @Test
    void givenValidRequest_whenGetRooms_thenReturnsAllRooms() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");

        when(findAllRoomsHandler.handle(any(FindAllRoomsQuery.class))).thenReturn(Collections.singletonList(roomDTO));

        List<RoomDTO> response = roomController.getRooms();

        assertEquals(1, response.size());
        assertEquals(roomDTO, response.get(0));
    }

    @Test
    void givenException_whenAddRoom_thenFailsWithException() throws URISyntaxException {
        when(addRoomHandler.handle(any(AddRoomCommand.class))).thenThrow(new RoomException("Handler exception"));

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");

        RoomException exception = assertThrows(RoomException.class, () -> {
            roomController.addRoom(roomDTO);
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenException_whenUpdateRoom_thenFailsWithException() {
        when(updateRoomHandler.handle(any(UpdateRoomCommand.class))).thenThrow(new RoomException("Handler exception"));

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");

        RoomException exception = assertThrows(RoomException.class, () -> {
            roomController.updateRoom("Conference Room A", roomDTO);
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenException_whenDeleteRoom_thenFailsWithException() {
        when(deleteRoomHandler.handle(any(DeleteRoomCommand.class))).thenThrow(new RoomException("Handler exception"));

        RoomException exception = assertThrows(RoomException.class, () -> {
            roomController.deleteRoom("Conference Room A");
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenException_whenGetRoomByName_thenFailsWithException() {
        when(findRoomByNameHandler.handle(any(FindRoomByNameQuery.class))).thenThrow(new RoomException("Handler exception"));

        RoomException exception = assertThrows(RoomException.class, () -> {
            roomController.getRoomByName("Conference Room A");
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenException_whenGetRooms_thenFailsWithException() {
        when(findAllRoomsHandler.handle(any(FindAllRoomsQuery.class))).thenThrow(new RoomException("Handler exception"));

        RoomException exception = assertThrows(RoomException.class, () -> {
            roomController.getRooms();
        });

        assertEquals("Handler exception", exception.getMessage());
    }
}
