package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.command.handler.DisableRoomHandler;
import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.RoomsNotFoundException;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.FindAllRoomsQuery;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindAllRoomsHandler implements BaseQuery<FindAllRoomsQuery, List<RoomDTO>> {
    private static final Logger logger = LogManager.getLogger(FindAllRoomsHandler.class);

    private final RoomRepository roomRepository;

    @Autowired
    public FindAllRoomsHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomDTO> handle(FindAllRoomsQuery query) {
        List<Room> all = roomRepository.findAll();
        if (!all.isEmpty()) {
            return RoomDTO.from(all);
        }
        logger.error("Rooms not found");

        throw new RoomsNotFoundException(BookingError.ROOM_NOT_FOUND);
    }
}
