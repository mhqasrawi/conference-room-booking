package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.command.handler.DisableRoomHandler;
import com.mashreq.room.conference.booking.dto.MaintenanceDTO;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.exceptions.BookingError;
import com.mashreq.room.conference.booking.exceptions.MaintenanceTimingsException;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.GetMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindMaintenanceTimingsHandler implements BaseQuery<GetMaintenanceTimingsQuery, MaintenanceDTO> {

    private static final Logger logger = LogManager.getLogger(DisableRoomHandler.class);

    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public FindMaintenanceTimingsHandler(MaintenanceTimingRepository maintenanceTimingRepository) {
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }

    @Override
    public MaintenanceDTO handle(GetMaintenanceTimingsQuery query) {
        Long id = query.getId();

        MaintenanceTimings maintenanceTimings = maintenanceTimingRepository.findById(id)
                .orElseThrow(() -> new MaintenanceTimingsException(BookingError.MAINTENANCE_TIMINGS_NOT_FOUND));
        return new MaintenanceDTO(maintenanceTimings.getId(), maintenanceTimings.getStartTime().toString(),
                maintenanceTimings.getEndTime().toString());
    }
}
