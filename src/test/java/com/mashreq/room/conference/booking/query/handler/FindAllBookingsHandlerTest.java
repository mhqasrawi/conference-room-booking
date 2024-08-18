package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.BookingDTO;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.BookingException;
import com.mashreq.room.conference.booking.query.FindAllBookingsQuery;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FindAllBookingsHandlerTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private FindAllBookingsHandler findAllBookingsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleReturnsBookingsForValidDate() {
        Booking booking = new Booking();
        booking.setRoom(new Room());
        booking.setStartTime(LocalTime.parse("15:00"));
        booking.setEndTime(LocalTime.parse("15:15"));
        booking.setDate(LocalDate.now());
        List<Booking> bookings = Collections.singletonList(booking);
        when(bookingRepository.findByDate(any(LocalDate.class))).thenReturn(bookings);

        FindAllBookingsQuery query = new FindAllBookingsQuery("2023-10-01");
        List<BookingDTO> result = findAllBookingsHandler.handle(query);

        assertEquals(1, result.size());
    }

    @Test
    void handleThrowsExceptionWhenNoBookingsFound() {
        when(bookingRepository.findByDate(any(LocalDate.class))).thenReturn(Collections.emptyList());

        FindAllBookingsQuery query = new FindAllBookingsQuery("2023-10-01");
        assertThrows(BookingException.class, () -> findAllBookingsHandler.handle(query));
    }

    @Test
    void handleThrowsExceptionForInvalidDateFormat() {
        FindAllBookingsQuery query = new FindAllBookingsQuery("invalid-date");
        assertThrows(DateTimeParseException.class, () -> findAllBookingsHandler.handle(query));
    }
}