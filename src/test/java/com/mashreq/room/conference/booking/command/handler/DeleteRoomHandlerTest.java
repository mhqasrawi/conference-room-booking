package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.CommandResult;
import com.mashreq.room.conference.booking.command.DeleteRoomCommand;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class DeleteRoomHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private DeleteRoomHandler deleteRoomHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidRoomName_whenHandle_thenDeletesRoom() {
        String roomName = "Conference Room A";
        DeleteRoomCommand command = new DeleteRoomCommand(roomName);
        Room room = new Room();
        room.setName(roomName);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).delete(room);

        String result = deleteRoomHandler.handle(command);

        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void givenNonExistentRoomName_whenHandle_thenThrowsRoomException() {
        String roomName = "Non Existent Room";
        DeleteRoomCommand command = new DeleteRoomCommand(roomName);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());

        RoomException exception = assertThrows(RoomException.class, () -> {
            deleteRoomHandler.handle(command);
        });

        assertEquals(BookingError.ROOM_NOT_FOUND.getCode(), exception.getCode().getCode());
    }
}