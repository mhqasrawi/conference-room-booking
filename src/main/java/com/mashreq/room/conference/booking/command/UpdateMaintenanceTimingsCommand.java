package com.mashreq.room.conference.booking.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UpdateMaintenanceTimingsCommand implements Command {
    private Long id;
    private String startTime;
    private String endTime;

}
