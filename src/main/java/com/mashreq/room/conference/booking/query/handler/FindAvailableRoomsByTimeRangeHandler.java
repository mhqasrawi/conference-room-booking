package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.FindAvailableRoomsByTimeRangeQuery;
import com.mashreq.room.conference.booking.repositories.BookingRepository;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FindAvailableRoomsByTimeRangeHandler implements BaseQuery<FindAvailableRoomsByTimeRangeQuery, List<RoomDTO> > {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public FindAvailableRoomsByTimeRangeHandler(RoomRepository roomRepository, BookingRepository bookingRepository, MaintenanceTimingRepository maintenanceTimingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }
    @Override
    public List<RoomDTO> handle(FindAvailableRoomsByTimeRangeQuery query) {
        LocalTime startTime = LocalTime.parse(query.getStartTime());
        LocalTime endTime = LocalTime.parse(query.getEndTime());

        List<Room> allRooms = roomRepository.findAll();

        List<Room> availableRooms = allRooms.stream()
                .filter(room -> bookingRepository.findByRoomNameAndStartTimeLessThanAndEndTimeGreaterThan(room.getName(),
                        endTime, startTime).isEmpty())
                .filter(room -> maintenanceTimingRepository.findByStartTimeLessThanAndEndTimeGreaterThan(endTime, startTime).isEmpty())
                .toList();

        return RoomDTO.from(availableRooms);
    }
}
