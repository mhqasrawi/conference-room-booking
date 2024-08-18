package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.AddRoomCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AddRoomHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private AddRoomHandler addRoomHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidCommand_whenHandle_thenSavesRoom() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");
        roomDTO.setCapacity(10);
        AddRoomCommand command = new AddRoomCommand(roomDTO);

        Room room = new Room();
        room.setName("Conference Room A");
        room.setCapacity(10);
        room.setId(1L);

        when(roomRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        String roomName = addRoomHandler.handle(command);

        assertEquals("Conference Room A", roomName);
    }

    @Test
    void givenExistingRoomName_whenHandle_thenThrowsRoomException() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName("Conference Room A");
        roomDTO.setCapacity(10);
        AddRoomCommand command = new AddRoomCommand(roomDTO);

        Room existingRoom = new Room();
        existingRoom.setName("Conference Room A");

        when(roomRepository.findByName(any(String.class))).thenReturn(Optional.of(existingRoom));

        RoomException exception = assertThrows(RoomException.class, () -> {
            addRoomHandler.handle(command);
        });

        assertEquals(BookingError.ROOM_ALREADY_EXISTS.getCode(), exception.getCode().getCode());
    }
}