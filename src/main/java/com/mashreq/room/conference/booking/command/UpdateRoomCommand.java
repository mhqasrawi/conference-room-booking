package com.mashreq.room.conference.booking.command;

import com.mashreq.room.conference.booking.dto.RoomDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomCommand implements Command {
    private String name;
    private RoomDTO roomDTO;

}
