package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.command.AddBookingCommand;
import com.mashreq.room.conference.booking.command.handler.AddBookingHandler;
import com.mashreq.room.conference.booking.dto.BookingDTO;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.exceptions.ErrorCode;
import com.mashreq.room.conference.booking.query.FindAllBookingsQuery;
import com.mashreq.room.conference.booking.query.FindBookingsByBookedByQuery;
import com.mashreq.room.conference.booking.query.FindBookingsByRoomNameQuery;
import com.mashreq.room.conference.booking.query.handler.FindAllBookingsHandler;
import com.mashreq.room.conference.booking.query.handler.FindBookingsByBookedByHandler;
import com.mashreq.room.conference.booking.query.handler.FindBookingsByRoomNameHandler;
import com.mashreq.room.conference.booking.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/bookings")
@Validated
public class BookingController {

    private final AddBookingHandler addBookingHandler;
    private final FindAllBookingsHandler findAllBookingsHandler;
    private final FindBookingsByRoomNameHandler findBookingsByRoomNameHandler;
    private final FindBookingsByBookedByHandler findBookingsByBookedByHandler;

    @Autowired
    public BookingController(AddBookingHandler addBookingHandler, FindAllBookingsHandler findAllBookingsHandler, FindBookingsByRoomNameHandler findBookingsByRoomNameHandler, FindBookingsByBookedByHandler findBookingsByBookedByHandler) {
        this.addBookingHandler = addBookingHandler;
        this.findAllBookingsHandler = findAllBookingsHandler;
        this.findBookingsByRoomNameHandler = findBookingsByRoomNameHandler;
        this.findBookingsByBookedByHandler = findBookingsByBookedByHandler;
    }


    @GetMapping
    @Operation(summary = "Get all bookings on current date or on given date", description = "Get all bookings on current date or on given date" +
            "timings",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description =
                            "Bookings"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "booking" +
                            " " +
                            "not found",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security = {@SecurityRequirement(name = "bearer-key")})
    public List<BookingDTO> getAllBookings(@RequestParam(required = false) String date) {
        String searchDate;
        if (Objects.isNull(date)) {
            searchDate = LocalDate.now().toString();
        } else {
            searchDate = date;
        }
        return findAllBookingsHandler.handle(new FindAllBookingsQuery(searchDate));
    }

    @PostMapping
    @Operation(summary = "Create booking on given time and number of attendee",
            description = "Create booking on given time and number of attendee",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description =
                            "Bookings"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security = {@SecurityRequirement(name = "bearer-key")})
    public Long addBooking(@RequestBody BookingDTO booking) {

      return   this.addBookingHandler.handle(new AddBookingCommand(booking.getNumberOfAttendees(),
                booking.getStartTime(), booking.getEndTime(), SecurityUtils.getCurrentUser()));
    }


    @GetMapping("/room/{roomName}")
    @Operation(summary = "get booking by room name",
            description = "get booking by room name",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description =
                            "Bookings"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security = {@SecurityRequirement(name = "bearer-key")})
    public List<BookingDTO> getBookingsByRoom(@PathVariable("roomName") String roomName) {
        return this.findBookingsByRoomNameHandler.handle(new FindBookingsByRoomNameQuery(roomName));
    }

    @GetMapping("/booked-by/{bookedBy}")
    @Operation(summary = "get booking by BookedBy",
            description = "get booking by BookedBy",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description =
                            "Bookings"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema =
                            @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorCode.class)))}, security = {@SecurityRequirement(name = "bearer-key")})
    public List<BookingDTO> getBookingsByBookedBy(@PathVariable("bookedBy") String bookedBy) {
        return this.findBookingsByBookedByHandler.handle(new FindBookingsByBookedByQuery(bookedBy));
    }


}