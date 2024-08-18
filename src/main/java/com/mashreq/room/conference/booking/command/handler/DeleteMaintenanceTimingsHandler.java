package com.mashreq.room.conference.booking.command.handler;

import com.mashreq.room.conference.booking.command.BaseCommand;
import com.mashreq.room.conference.booking.command.CommandResult;
import com.mashreq.room.conference.booking.command.DeleteMaintenanceTimingsCommand;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.MaintenanceTimingsException;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeleteMaintenanceTimingsHandler implements BaseCommand<DeleteMaintenanceTimingsCommand, String> {
    private static final Logger logger = LogManager.getLogger(AddMaintenanceTimingsHandler.class);
    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public DeleteMaintenanceTimingsHandler(MaintenanceTimingRepository maintenanceTimingRepository) {
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }

    @Override
    public String handle(DeleteMaintenanceTimingsCommand command) {
        Long id = command.getId();
        Optional<MaintenanceTimings> byId = maintenanceTimingRepository.findById(id);
        if (byId.isPresent()) {
            maintenanceTimingRepository.deleteById(id);
        } else {
            logger.error("Maintenance Timings with id {} not found", id);
            throw new MaintenanceTimingsException(BookingError.MAINTENANCE_TIMINGS_NOT_FOUND);
        }
        return CommandResult.SUCCESS;
    }
}
