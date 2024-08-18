package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.AddMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AddMaintenanceTimingsHandlerTest {

    @Mock
    private MaintenanceTimingRepository maintenanceTimingRepository;

    @InjectMocks
    private AddMaintenanceTimingsHandler addMaintenanceTimingsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidCommand_whenHandle_thenSavesMaintenanceTimings() {
        AddMaintenanceTimingsCommand command = new AddMaintenanceTimingsCommand("10:00", "12:00");
        MaintenanceTimings maintenanceTimings = new MaintenanceTimings(LocalTime.parse("10:00"), LocalTime.parse("12" +
                ":00"));
        maintenanceTimings.setId(1L);

        when(maintenanceTimingRepository.save(any(MaintenanceTimings.class))).thenReturn(maintenanceTimings);

        Long id = addMaintenanceTimingsHandler.handle(command);

        assertEquals(1L, id);
    }




}