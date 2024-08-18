package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.command.UpdateRoomCommand;
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
public class UpdateRoomHandler implements BaseCommand<UpdateRoomCommand, RoomDTO> {

    private static final Logger logger = LogManager.getLogger(UpdateRoomHandler.class);
    private final RoomRepository roomRepository;

    @Autowired
    public UpdateRoomHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO handle(UpdateRoomCommand command) {
        String name = command.getName();
        RoomDTO roomDTO = command.getRoomDTO();
        Optional<Room> byName = roomRepository.findByName(name);
        if (byName.isPresent()) {
            Room room = byName.get();
            if (room.isDisabled()) {
                logger.error("Room with name {} is disabled", name);
                throw new RoomException(BookingError.ROOM_DISABLED);
            }
            room.setCapacity(roomDTO.getCapacity());
            Room savedRoom = roomRepository.save(room);
            return RoomDTO.from(savedRoom);
        }
        logger.error("Room with name {} not found", name);
        throw new RoomException(BookingError.ROOM_NOT_FOUND);
    }
}
