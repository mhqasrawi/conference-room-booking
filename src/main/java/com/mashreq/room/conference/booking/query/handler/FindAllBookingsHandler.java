package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.command.handler.AddMaintenanceTimingsHandler;
import com.mashreq.room.conference.booking.dto.BookingDTO;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.BookingException;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.FindAllBookingsQuery;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FindAllBookingsHandler implements BaseQuery<FindAllBookingsQuery, List<BookingDTO>> {
    private static final Logger logger = LogManager.getLogger(FindAllBookingsHandler.class);
    private final BookingRepository bookingRepository;

    @Autowired
    public FindAllBookingsHandler(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<BookingDTO> handle(FindAllBookingsQuery query) {
        List<Booking> byDate = bookingRepository.findByDate(LocalDate.parse(query.getSearchDate()));
        if(byDate.isEmpty()){
            logger.error("Booking with date {} not found", query.getSearchDate());
            throw new BookingException(BookingError.BOOKING_NOT_FOUND);
        }
        return BookingDTO.from(byDate);

    }
}
