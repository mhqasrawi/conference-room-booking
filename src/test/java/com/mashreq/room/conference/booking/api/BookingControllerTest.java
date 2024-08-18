package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.command.AddBookingCommand;
import com.mashreq.room.conference.booking.command.handler.AddBookingHandler;
import com.mashreq.room.conference.booking.dto.BookingDTO;
import com.mashreq.room.conference.booking.query.FindAllBookingsQuery;
import com.mashreq.room.conference.booking.query.FindBookingsByBookedByQuery;
import com.mashreq.room.conference.booking.query.FindBookingsByRoomNameQuery;
import com.mashreq.room.conference.booking.query.handler.FindAllBookingsHandler;
import com.mashreq.room.conference.booking.query.handler.FindBookingsByBookedByHandler;
import com.mashreq.room.conference.booking.query.handler.FindBookingsByRoomNameHandler;
import com.mashreq.room.conference.booking.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddBookingHandler addBookingHandler;

    @Mock
    private FindAllBookingsHandler findAllBookingsHandler;

    @Mock
    private FindBookingsByRoomNameHandler findBookingsByRoomNameHandler;

    @Mock
    private FindBookingsByBookedByHandler findBookingsByBookedByHandler;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void getAllBookingsReturnsBookingsForGivenDate() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        List<BookingDTO> bookings = Collections.singletonList(bookingDTO);
        when(findAllBookingsHandler.handle(any(FindAllBookingsQuery.class))).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                .param("date", "2023-10-01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void getAllBookingsReturnsBookingsForCurrentDateWhenDateIsNull() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        List<BookingDTO> bookings = Collections.singletonList(bookingDTO);
        when(findAllBookingsHandler.handle(any(FindAllBookingsQuery.class))).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void addBookingCreatesNewBooking() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setNumberOfAttendees(10);
        bookingDTO.setStartTime("10:00");
        bookingDTO.setEndTime("11:00");

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numberOfAttendees\":10,\"startTime\":\"10:00\",\"endTime\":\"11:00\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByRoomReturnsBookingsForGivenRoom() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        List<BookingDTO> bookings = Collections.singletonList(bookingDTO);
        when(findBookingsByRoomNameHandler.handle(any(FindBookingsByRoomNameQuery.class))).thenReturn(bookings);

        mockMvc.perform(get("/bookings/room/ConferenceRoomA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void getBookingsByBookedByReturnsBookingsForGivenUser() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        List<BookingDTO> bookings = Collections.singletonList(bookingDTO);
        when(findBookingsByBookedByHandler.handle(any(FindBookingsByBookedByQuery.class))).thenReturn(bookings);

        mockMvc.perform(get("/bookings/booked-by/johndoe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}