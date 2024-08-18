package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.AddBookingCommand;
import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.entities.Booking;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.BookingException;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AddBookingHandler implements BaseCommand<AddBookingCommand, Long> {
    private static final Logger logger = LogManager.getLogger(AddBookingHandler.class);
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public AddBookingHandler(BookingRepository bookingRepository, RoomRepository roomRepository, MaintenanceTimingRepository maintenanceTimingRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }

    @Override
    public Long handle(AddBookingCommand command) {
        List<Room> rooms = getAvailableRooms(command.getNumberOfAttendees());
        List<String> roomNames = rooms.stream().map(Room::getName).toList();
        List<Booking> byDateAndStartTimeLessThanAndEndTimeGreaterThan = bookingRepository.findByDateAndStartTimeLessThanAndEndTimeGreaterThanAndRoomNameIn(LocalDate.now(), LocalTime.parse(command.getEndTime()), LocalTime.parse(command.getStartTime()), roomNames);


        String availableRoomName = roomNames.stream().filter(r -> byDateAndStartTimeLessThanAndEndTimeGreaterThan.stream().noneMatch(b -> b.getRoomName().equals(r))).findFirst().orElseThrow(() -> new BookingException(BookingError.FULLY_BOOKED_AT_GIVEN_TIME));


        List<MaintenanceTimings> byStartTimeLessThanAndEndTimeGreaterThan = maintenanceTimingRepository.findByStartTimeLessThanAndEndTimeGreaterThan(LocalTime.parse(command.getEndTime()), LocalTime.parse(command.getStartTime()));

        if (!byStartTimeLessThanAndEndTimeGreaterThan.isEmpty()) {
            logger.error("Requested time overlaps with maintenance time");
            throw new BookingException(BookingError.MAINTENANCE_TIME_OVERLAP);
        }

            Booking booking = new Booking();
            booking.setDate(LocalDate.now());
            booking.setStartTime(LocalTime.parse(command.getStartTime()));
            booking.setEndTime(LocalTime.parse(command.getEndTime()));
            booking.setBookedBy(command.getUsername());
            booking.setRoom(rooms.stream().filter(r -> r.getName().equals(availableRoomName)).findFirst().orElseThrow(() -> new BookingException(BookingError.ROOM_NOT_AVAILABLE)));
            booking.setRoomName(availableRoomName);
            booking.setNumberOfAttendees(command.getNumberOfAttendees());
            Booking saved = this.bookingRepository.save(booking);
            return saved.getId();
    }

    private List<Room> getAvailableRooms(int numberOfAttendees) {
        List<Room> byCapacityGreaterThanEqual = this.roomRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(numberOfAttendees);
        if (!byCapacityGreaterThanEqual.isEmpty()) {
            return byCapacityGreaterThanEqual;
        }
        logger.error("Room not available for given number of attendees");
        throw new BookingException(BookingError.ROOM_NOT_AVAILABLE);
    }
}
