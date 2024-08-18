package com.mashreq.room.conference.booking.query.handler;

import com.mashreq.room.conference.booking.dto.MaintenanceDTO;
import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.query.BaseQuery;
import com.mashreq.room.conference.booking.query.GetAllMaintenanceTimingsQuery;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FindAllMaintenanceTimingsHandler implements BaseQuery<GetAllMaintenanceTimingsQuery, List<MaintenanceDTO>> {


    private final MaintenanceTimingRepository maintenanceTimingRepository;

    @Autowired
    public FindAllMaintenanceTimingsHandler(MaintenanceTimingRepository maintenanceTimingRepository) {
        this.maintenanceTimingRepository = maintenanceTimingRepository;
    }

    @Override
    public List<MaintenanceDTO> handle(GetAllMaintenanceTimingsQuery query) {
        List<MaintenanceTimings> maintenanceTimingsList = maintenanceTimingRepository.findAll();
        return maintenanceTimingsList.stream()
                .map(mt -> new MaintenanceDTO(mt.getId(), mt.getStartTime().toString(),
                        mt.getEndTime().toString()))
                .collect(Collectors.toList());
    }
}
