package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.MaintenanceDTO;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.query.GetAllMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FindAllMaintenanceTimingsHandlerTest {

    @Mock
    private MaintenanceTimingRepository maintenanceTimingRepository;

    @InjectMocks
    private FindAllMaintenanceTimingsHandler findAllMaintenanceTimingsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidQuery_whenHandle_thenReturnsMaintenanceDTOList() {
        MaintenanceTimings mt1 = new MaintenanceTimings( LocalTime.parse("08:00"), LocalTime.parse("10:00"));
        MaintenanceTimings mt2 = new MaintenanceTimings( LocalTime.parse("10:00"), LocalTime.parse("12:00"));
        mt1.setId(1L);
        mt2.setId(2L);
        List<MaintenanceTimings> maintenanceTimingsList = Arrays.asList(mt1, mt2);

        when(maintenanceTimingRepository.findAll()).thenReturn(maintenanceTimingsList);

        GetAllMaintenanceTimingsQuery query = new GetAllMaintenanceTimingsQuery();
        List<MaintenanceDTO> result = findAllMaintenanceTimingsHandler.handle(query);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("08:00", result.get(0).getStartTime());
        assertEquals("10:00", result.get(0).getEndTime());
    }

    @Test
    void givenEmptyRepository_whenHandle_thenReturnsEmptyList() {
        when(maintenanceTimingRepository.findAll()).thenReturn(List.of());

        GetAllMaintenanceTimingsQuery query = new GetAllMaintenanceTimingsQuery();
        List<MaintenanceDTO> result = findAllMaintenanceTimingsHandler.handle(query);

        assertEquals(0, result.size());
    }
}