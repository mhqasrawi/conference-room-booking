package com.mashreq.room.conference.booking.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddBookingCommand implements Command {
    private int numberOfAttendees;
    private String startTime;
    private String endTime;
    private String username;
}
