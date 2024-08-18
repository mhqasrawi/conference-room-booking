package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.command.AddMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.DeleteMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.UpdateMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.handler.AddMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.command.handler.DeleteMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.command.handler.UpdateMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.dto.MaintenanceDTO;
import com.mashreq.room.conference.booking.exceptions.MaintenanceTimingsException;
import com.mashreq.room.conference.booking.query.GetAllMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.query.GetMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.query.handler.FindAllMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.query.handler.FindMaintenanceTimingsHandler;
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

class MaintenanceControllerTest {

    @Mock
    private AddMaintenanceTimingsHandler addMaintenanceTimingsHandler;

    @Mock
    private DeleteMaintenanceTimingsHandler deleteMaintenanceTimingsHandler;

    @Mock
    private UpdateMaintenanceTimingsHandler updateMaintenanceTimingsHandler;

    @Mock
    private FindAllMaintenanceTimingsHandler findAllMaintenanceTimingsHandler;

    @Mock
    private FindMaintenanceTimingsHandler findMaintenanceTimingsHandler;

    @InjectMocks
    private MaintenanceController maintenanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidTiming_whenAddMaintenanceTimings_thenCreatesNewTiming() throws URISyntaxException {
        when(addMaintenanceTimingsHandler.handle(any(AddMaintenanceTimingsCommand.class))).thenReturn(1L);

        MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
        maintenanceDTO.setStartTime("2023-10-01T10:00:00");
        maintenanceDTO.setEndTime("2023-10-01T12:00:00");

        ResponseEntity<Void> response = maintenanceController.addMaintenanceTimings(maintenanceDTO);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void givenValidTiming_whenUpdateMaintenanceTimings_thenUpdatesExistingTiming() {
        MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
        maintenanceDTO.setStartTime("2023-10-01T10:00:00");
        maintenanceDTO.setEndTime("2023-10-01T12:00:00");

        ResponseEntity<Void> response = maintenanceController.updateMaintenanceTimings(1L, maintenanceDTO);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void givenValidId_whenDeleteMaintenanceTimings_thenDeletesTiming() {
        ResponseEntity<Void> response = maintenanceController.deleteMaintenanceTimings(1L);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void givenValidId_whenGetMaintenanceTimings_thenReturnsTimingById() {
        MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
        maintenanceDTO.setStartTime("2023-10-01T10:00:00");
        maintenanceDTO.setEndTime("2023-10-01T12:00:00");

        when(findMaintenanceTimingsHandler.handle(any(GetMaintenanceTimingsQuery.class))).thenReturn(maintenanceDTO);

        ResponseEntity<MaintenanceDTO> response = maintenanceController.getMaintenanceTimings(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(maintenanceDTO, response.getBody());
    }

    @Test
    void givenTimingsExist_whenGetAllMaintenanceTimings_thenReturnsAllTimings() {
        MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
        maintenanceDTO.setStartTime("2023-10-01T10:00:00");
        maintenanceDTO.setEndTime("2023-10-01T12:00:00");

        when(findAllMaintenanceTimingsHandler.handle(any(GetAllMaintenanceTimingsQuery.class))).thenReturn(Collections.singletonList(maintenanceDTO));

        ResponseEntity<List<MaintenanceDTO>> response = maintenanceController.getAllMaintenanceTimings();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(maintenanceDTO, response.getBody().get(0));
    }

    @Test
    void givenHandlerThrowsException_whenAddMaintenanceTimings_thenFailsWithException() throws URISyntaxException {
        when(addMaintenanceTimingsHandler.handle(any(AddMaintenanceTimingsCommand.class))).thenThrow(new MaintenanceTimingsException("Handler exception"));

        MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
        maintenanceDTO.setStartTime("2023-10-01T10:00:00");
        maintenanceDTO.setEndTime("2023-10-01T12:00:00");

        MaintenanceTimingsException exception = assertThrows(MaintenanceTimingsException.class, () -> {
            maintenanceController.addMaintenanceTimings(maintenanceDTO);
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenHandlerThrowsException_whenUpdateMaintenanceTimings_thenFailsWithException() {
        when(updateMaintenanceTimingsHandler.handle(any(UpdateMaintenanceTimingsCommand.class))).thenThrow(new MaintenanceTimingsException("Handler exception"));

        MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
        maintenanceDTO.setStartTime("2023-10-01T10:00:00");
        maintenanceDTO.setEndTime("2023-10-01T12:00:00");

        MaintenanceTimingsException exception = assertThrows(MaintenanceTimingsException.class, () -> {
            maintenanceController.updateMaintenanceTimings(1L, maintenanceDTO);
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenHandlerThrowsException_whenDeleteMaintenanceTimings_thenFailsWithException() {
        when(deleteMaintenanceTimingsHandler.handle(any(DeleteMaintenanceTimingsCommand.class))).thenThrow(new MaintenanceTimingsException("Handler exception"));

        MaintenanceTimingsException exception = assertThrows(MaintenanceTimingsException.class, () -> {
            maintenanceController.deleteMaintenanceTimings(1L);
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenHandlerThrowsException_whenGetMaintenanceTimings_thenFailsWithException() {
        when(findMaintenanceTimingsHandler.handle(any(GetMaintenanceTimingsQuery.class))).thenThrow(new MaintenanceTimingsException("Handler exception"));

        MaintenanceTimingsException exception = assertThrows(MaintenanceTimingsException.class, () -> {
            maintenanceController.getMaintenanceTimings(1L);
        });

        assertEquals("Handler exception", exception.getMessage());
    }

    @Test
    void givenHandlerThrowsException_whenGetAllMaintenanceTimings_thenFailsWithException() {
        when(findAllMaintenanceTimingsHandler.handle(any(GetAllMaintenanceTimingsQuery.class))).thenThrow(new MaintenanceTimingsException("Handler exception"));

        MaintenanceTimingsException exception = assertThrows(MaintenanceTimingsException.class, () -> {
            maintenanceController.getAllMaintenanceTimings();
        });

        assertEquals("Handler exception", exception.getMessage());
    }
}
