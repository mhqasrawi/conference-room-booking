package com.mashreq.room.conference.booking.dto;

import com.mashreq.room.conference.booking.entities.Room;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoomDTO {

    @NotEmpty(message = "Room name cannot be empty")
    @Length(min = 3, max = 50, message = "Room name should be between 3 and 50 characters")
    private String name;
    @Positive
    private int capacity;
    private boolean isDisabled = false;

    public static List<RoomDTO> from(List<Room> all) {
        List<RoomDTO> rooms = new ArrayList<>();
        for (Room room : all) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setName(room.getName());
            roomDTO.setCapacity(room.getCapacity());
            roomDTO.setDisabled(room.isDisabled());
            rooms.add(roomDTO);
        }
        return rooms;
    }

    public static RoomDTO from(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName(room.getName());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setDisabled(room.isDisabled());
        return roomDTO;
    }
}
