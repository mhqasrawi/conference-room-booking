package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.AddRoomCommand;
import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.dto.RoomDTO;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.RoomException;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component

public class AddRoomHandler implements BaseCommand<AddRoomCommand, String> {

    private static final Logger logger = LogManager.getLogger(AddRoomHandler.class);
    private final RoomRepository roomRepository;

    @Autowired
    public AddRoomHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public String handle(AddRoomCommand command) {
        RoomDTO roomDTO = command.getRoomDTO();
        Optional<Room> byName = roomRepository.findByName(roomDTO.getName());
        if (byName.isPresent()) {
            logger.error("Room with name {} already exists", roomDTO.getName());
            throw new RoomException(BookingError.ROOM_ALREADY_EXISTS);
        }
        Room room = new Room();
        room.setName(roomDTO.getName());
        room.setCapacity(roomDTO.getCapacity());
        Room savedRoom = roomRepository.save(room);
        return savedRoom.getName();
    }
}
