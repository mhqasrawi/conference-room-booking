package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.CommandResult;
import com.mashreq.room.conference.booking.command.DeleteMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.MaintenanceTimingsException;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
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

class DeleteMaintenanceTimingsHandlerTest {

    @Mock
    private MaintenanceTimingRepository maintenanceTimingRepository;

    @InjectMocks
    private DeleteMaintenanceTimingsHandler deleteMaintenanceTimingsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidId_whenHandle_thenDeletesMaintenanceTimings() {
        Long id = 1L;
        DeleteMaintenanceTimingsCommand command = new DeleteMaintenanceTimingsCommand(id);
        MaintenanceTimings maintenanceTimings = new MaintenanceTimings();
        maintenanceTimings.setId(id);

        when(maintenanceTimingRepository.findById(id)).thenReturn(Optional.of(maintenanceTimings));
        doNothing().when(maintenanceTimingRepository).deleteById(id);

        String result = deleteMaintenanceTimingsHandler.handle(command);

        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    void givenInvalidId_whenHandle_thenThrowsMaintenanceTimingsException() {
        Long id = 1L;
        DeleteMaintenanceTimingsCommand command = new DeleteMaintenanceTimingsCommand(id);

        when(maintenanceTimingRepository.findById(id)).thenReturn(Optional.empty());

        MaintenanceTimingsException exception = assertThrows(MaintenanceTimingsException.class, () -> {
            deleteMaintenanceTimingsHandler.handle(command);
        });

        assertEquals(BookingError.MAINTENANCE_TIMINGS_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }
}