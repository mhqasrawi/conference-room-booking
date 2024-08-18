package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.UpdateRoomCommand;
import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.RoomException;
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
import static org.mockito.Mockito.verify;

class UpdateRoomHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private UpdateRoomHandler updateRoomHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidRoomNameAndDTO_whenHandle_thenUpdatesRoom() {
        String roomName = "Conference Room A";
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setCapacity(50);
        UpdateRoomCommand command = new UpdateRoomCommand(roomName, roomDTO);
        Room room = new Room();
        room.setName(roomName);
        room.setCapacity(30);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        RoomDTO result = updateRoomHandler.handle(command);

        assertEquals(50, result.getCapacity());
        verify(roomRepository).save(room);
    }

    @Test
    void givenNonExistentRoomName_whenHandle_thenThrowsRoomException() {
        String roomName = "Non Existent Room";
        RoomDTO roomDTO = new RoomDTO();
        UpdateRoomCommand command = new UpdateRoomCommand(roomName, roomDTO);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());

        RoomException exception = assertThrows(RoomException.class, () -> {
            updateRoomHandler.handle(command);
        });

        assertEquals(BookingError.ROOM_NOT_FOUND.getCode(), exception.getCode().getCode());
    }

    @Test
    void givenDisabledRoom_whenHandle_thenThrowsRoomException() {
        String roomName = "Disabled Room";
        RoomDTO roomDTO = new RoomDTO();
        UpdateRoomCommand command = new UpdateRoomCommand(roomName, roomDTO);
        Room room = new Room();
        room.setName(roomName);
        room.setDisabled(true);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));

        RoomException exception = assertThrows(RoomException.class, () -> {
            updateRoomHandler.handle(command);
        });

        assertEquals(BookingError.ROOM_DISABLED.getCode(), exception.getCode().getCode());
    }
}