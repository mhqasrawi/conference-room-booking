package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.query.FindAvailableRoomsByTimeRangeQuery;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FindAvailableRoomsByTimeRangeHandlerTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private MaintenanceTimingRepository maintenanceTimingRepository;

    @InjectMocks
    private FindAvailableRoomsByTimeRangeHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnsAvailableRoomsWhenNoBookingsOrMaintenance() {
        Room room = new Room();
        room.setName("Room A");
        room.setCapacity(10);
        List<Room> rooms = Collections.singletonList(room);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(bookingRepository.findByRoomNameAndStartTimeLessThanAndEndTimeGreaterThan(any(), any(), any())).thenReturn(Collections.emptyList());
        when(maintenanceTimingRepository.findByStartTimeLessThanAndEndTimeGreaterThan(any(), any())).thenReturn(Collections.emptyList());

        FindAvailableRoomsByTimeRangeQuery query = new FindAvailableRoomsByTimeRangeQuery("09:00", "10:00");
        List<RoomDTO> result = handler.handle(query);

        assertEquals(1, result.size());
        assertEquals("Room A", result.get(0).getName());
    }

    @Test
    void returnsEmptyListWhenAllRoomsBooked() {
        Room room = new Room();
        room.setName("Room A");
        room.setCapacity(10);
        List<Room> rooms = Collections.singletonList(room);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(bookingRepository.findByRoomNameAndStartTimeLessThanAndEndTimeGreaterThan(any(), any(), any())).thenReturn(Collections.singletonList(new Booking()));
        when(maintenanceTimingRepository.findByStartTimeLessThanAndEndTimeGreaterThan(any(), any())).thenReturn(Collections.emptyList());

        FindAvailableRoomsByTimeRangeQuery query = new FindAvailableRoomsByTimeRangeQuery("09:00", "10:00");
        List<RoomDTO> result = handler.handle(query);

        assertEquals(0, result.size());
    }

    @Test
    void returnsEmptyListWhenAllRoomsUnderMaintenance() {
        Room room = new Room();
        room.setName("Room A");
        room.setCapacity(10);
        List<Room> rooms = Collections.singletonList(room);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(bookingRepository.findByRoomNameAndStartTimeLessThanAndEndTimeGreaterThan(any(), any(), any())).thenReturn(Collections.emptyList());
        when(maintenanceTimingRepository.findByStartTimeLessThanAndEndTimeGreaterThan(any(), any())).thenReturn(Collections.singletonList(new MaintenanceTimings()));

        FindAvailableRoomsByTimeRangeQuery query = new FindAvailableRoomsByTimeRangeQuery("09:00", "10:00");
        List<RoomDTO> result = handler.handle(query);

        assertEquals(0, result.size());
    }

    @Test
    void returnsEmptyListWhenNoRoomsAvailable() {
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        FindAvailableRoomsByTimeRangeQuery query = new FindAvailableRoomsByTimeRangeQuery("09:00", "10:00");
        List<RoomDTO> result = handler.handle(query);

        assertEquals(0, result.size());
    }
}