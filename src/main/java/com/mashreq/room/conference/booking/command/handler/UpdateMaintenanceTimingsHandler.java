package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.command.UpdateMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class UpdateMaintenanceTimingsHandler implements BaseCommand<UpdateMaintenanceTimingsCommand, Long> {
    private static final Logger logger = LogManager.getLogger(UpdateMaintenanceTimingsHandler.class);
    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public UpdateMaintenanceTimingsHandler(MaintenanceTimingRepository maintenanceTimingRepository) {
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }

    @Override
    public Long handle(UpdateMaintenanceTimingsCommand command) {
        LocalTime startTime = LocalTime.parse(command.getStartTime());
        LocalTime endTime = LocalTime.parse(command.getEndTime());
        MaintenanceTimings maintenanceTimings = new MaintenanceTimings(startTime, endTime);
        MaintenanceTimings saved = maintenanceTimingRepository.save(maintenanceTimings);
        return saved.getId();
    }
}
