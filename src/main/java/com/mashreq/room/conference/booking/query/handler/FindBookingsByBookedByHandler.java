package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.BookingDTO;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.BookingException;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.FindBookingsByBookedByQuery;
import com.mashreq.room.conference.booking.query.FindBookingsByRoomNameQuery;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindBookingsByBookedByHandler implements BaseQuery<FindBookingsByBookedByQuery, List<BookingDTO>> {

    private static final Logger logger = LogManager.getLogger(FindBookingsByBookedByHandler.class);
    private final BookingRepository bookingRepository;

    @Autowired
    public FindBookingsByBookedByHandler(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    @Override
    public List<BookingDTO> handle(FindBookingsByBookedByQuery query) {
        List<Booking> byRoomName = bookingRepository.findByBookedBy(query.getBookedBy());

        if(byRoomName.isEmpty()){
            throw new BookingException(BookingError.BOOKING_NOT_FOUND);
        }
        return BookingDTO.from(byRoomName);
    }
}
