package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.BookingDTO;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.BookingException;
import com.mashreq.room.conference.booking.query.FindBookingsByBookedByQuery;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FindBookingsByBookedByHandlerTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private FindBookingsByBookedByHandler findBookingsByBookedByHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleReturnsBookingsForValidUser() {
        Booking booking = new Booking();
        booking.setRoomName("ConferenceRoomA");
        Room room = new Room();
        room.setName("ConferenceRoomA");
        booking.setRoom(room);
        booking.setStartTime(LocalTime.parse("15:00"));
        booking.setEndTime(LocalTime.parse("15:15"));
        List<Booking> bookings = Collections.singletonList(booking);
        when(bookingRepository.findByBookedBy(anyString())).thenReturn(bookings);

        FindBookingsByBookedByQuery query = new FindBookingsByBookedByQuery("johndoe");
        List<BookingDTO> result = findBookingsByBookedByHandler.handle(query);

        assertEquals(1, result.size());
    }

    @Test
    void handleThrowsExceptionWhenNoBookingsFound() {
        when(bookingRepository.findByBookedBy(anyString())).thenReturn(Collections.emptyList());

        FindBookingsByBookedByQuery query = new FindBookingsByBookedByQuery("johndoe");
        assertThrows(BookingException.class, () -> findBookingsByBookedByHandler.handle(query));
    }

    @Test
    void handleThrowsExceptionForNullUser() {
        FindBookingsByBookedByQuery query = new FindBookingsByBookedByQuery(null);
        assertThrows(BookingException.class, () -> findBookingsByBookedByHandler.handle(query));
    }
}