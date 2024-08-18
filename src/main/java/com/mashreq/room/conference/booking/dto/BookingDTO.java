package com.mashreq.room.conference.booking.dto;

import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.entities.Room;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookingDTO {


    private String room;
    private LocalDate date;
    @NotEmpty
    private String startTime;
    @NotEmpty
    private String endTime;
    @NotEmpty
    @Min(2)
    private int numberOfAttendees;

    private String bookedBy;

    public static List<BookingDTO> from(List<Booking> all) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        for (Booking booking : all) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setRoom(booking.getRoom().getName());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStartTime(booking.getStartTime().toString());
            bookingDTO.setEndTime(booking.getEndTime().toString());
            bookingDTO.setNumberOfAttendees(booking.getNumberOfAttendees());
            bookingDTO.setBookedBy(booking.getBookedBy());
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }
}
