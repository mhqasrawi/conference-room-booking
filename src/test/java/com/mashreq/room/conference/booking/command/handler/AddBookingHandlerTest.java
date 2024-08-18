package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.AddBookingCommand;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingException;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AddBookingHandlerTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private MaintenanceTimingRepository maintenanceTimingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private AddBookingHandler addBookingHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreatesBookingWhenRoomIsAvailable() {
        when(bookingRepository.findByDateAndStartTimeLessThanAndEndTimeGreaterThanAndRoomNameIn(any(LocalDate.class),
                any(LocalTime.class), any(LocalTime.class),any(List.class)))
                .thenReturn(Collections.emptyList());
        Room room = new Room();
        room.setName("Amaze");
        when(roomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(any(Integer.class)))
                .thenReturn(Collections.singletonList(room));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(new Booking());

        AddBookingCommand command = new AddBookingCommand(10, "10:00", "11:00", "user");
        addBookingHandler.handle(command);
    }

    @Test
    void handleThrowsExceptionWhenRoomIsFullyBooked() {
        when(bookingRepository.findByDateAndStartTimeLessThanAndEndTimeGreaterThanAndRoomNameIn(any(LocalDate.class),
                any(LocalTime.class), any(LocalTime.class),any(List.class)))
                .thenReturn(Collections.singletonList(new Booking()));

        AddBookingCommand command = new AddBookingCommand(10, "10:00", "11:00", "user");
        assertThrows(BookingException.class, () -> addBookingHandler.handle(command));
    }

    @Test
    void handleThrowsExceptionWhenRoomNotAvailableForAttendees() {
        when(bookingRepository.findByDateAndStartTimeLessThanAndEndTimeGreaterThanAndRoomNameIn(any(LocalDate.class),
                any(LocalTime.class), any(LocalTime.class),any(List.class)))
                .thenReturn(Collections.emptyList());

        when(roomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(any(Integer.class)))
                .thenReturn(Collections.emptyList());

        AddBookingCommand command = new AddBookingCommand(10, "10:00", "11:00", "user");
        assertThrows(BookingException.class, () -> addBookingHandler.handle(command));
    }
}