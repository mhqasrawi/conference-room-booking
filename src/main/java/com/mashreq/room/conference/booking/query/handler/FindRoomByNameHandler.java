package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.RoomException;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.FindRoomByNameQuery;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindRoomByNameHandler implements BaseQuery<FindRoomByNameQuery, RoomDTO> {

    private static final Logger logger = LogManager.getLogger(FindRoomByNameHandler.class);
    private final RoomRepository roomRepository;

    @Autowired
    public FindRoomByNameHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO handle(FindRoomByNameQuery query) {
        String name = query.getName();
        Optional<Room> byName = roomRepository.findByName(name);
        if (byName.isPresent()) {
            return RoomDTO.from(byName.get());
        }
        logger.error("Room with name {} not found", name);
        throw new RoomException(BookingError.ROOM_NOT_FOUND);
    }
}
