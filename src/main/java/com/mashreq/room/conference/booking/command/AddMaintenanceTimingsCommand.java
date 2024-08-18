package com.mashreq.room.conference.booking.command;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddMaintenanceTimingsCommand implements Command {
    String startTime;
    String endTime;
}
