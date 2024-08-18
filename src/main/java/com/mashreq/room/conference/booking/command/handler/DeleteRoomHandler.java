package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.command.CommandResult;
import com.mashreq.room.conference.booking.command.DeleteRoomCommand;
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
public class DeleteRoomHandler implements BaseCommand<DeleteRoomCommand, String> {

    private static final Logger logger = LogManager.getLogger(DeleteRoomHandler.class);
    private final RoomRepository roomRepository;

    @Autowired
    public DeleteRoomHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public String handle(DeleteRoomCommand command) {
        String name = command.getName();
        Optional<Room> byName = roomRepository.findByName(name);
        if (byName.isPresent()) {
            roomRepository.delete(byName.get());
        } else {
            logger.error("Room with name {} not found", name);
            throw new RoomException(BookingError.ROOM_NOT_FOUND);
        }
        return CommandResult.SUCCESS;
    }
}
