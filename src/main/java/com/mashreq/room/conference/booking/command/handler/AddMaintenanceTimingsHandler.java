package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.AddMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class AddMaintenanceTimingsHandler implements BaseCommand<AddMaintenanceTimingsCommand, Long> {

    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public AddMaintenanceTimingsHandler(MaintenanceTimingRepository maintenanceTimingRepository) {
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }

    @Override
    public Long handle(AddMaintenanceTimingsCommand command) {
        String startTime = command.getStartTime();
        String endTime = command.getEndTime();
        MaintenanceTimings maintenanceTimings = new MaintenanceTimings(LocalTime.parse(startTime),
                LocalTime.parse(endTime));
        MaintenanceTimings saved = maintenanceTimingRepository.save(maintenanceTimings);
        return saved.getId();
    }
}
