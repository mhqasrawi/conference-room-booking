package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.CommandResult;
import com.mashreq.room.conference.booking.command.EnableRoomCommand;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EnableRoomHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private EnableRoomHandler enableRoomHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidRoomName_whenHandle_thenEnablesRoom() {
        String roomName = "Conference Room A";
        EnableRoomCommand command = new EnableRoomCommand(roomName);
        Room room = new Room();
        room.setName(roomName);
        room.setDisabled(true);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));

        String result = enableRoomHandler.handle(command);

        assertEquals(CommandResult.SUCCESS, result);
        verify(roomRepository).save(room);
    }

    @Test
    void givenNonExistentRoomName_whenHandle_thenThrowsRoomException() {
        String roomName = "Non Existent Room";
        EnableRoomCommand command = new EnableRoomCommand(roomName);

        when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());

        RoomException exception = assertThrows(RoomException.class, () -> {
            enableRoomHandler.handle(command);
        });

        assertEquals(BookingError.ROOM_NOT_FOUND.getCode(), exception.getCode().getCode());
    }
}