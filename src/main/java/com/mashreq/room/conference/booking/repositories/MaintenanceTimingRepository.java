package com.mashreq.room.conference.booking.repositories;

import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface MaintenanceTimingRepository extends JpaRepository<MaintenanceTimings, Long> {

    //9-9:15
    //9:10-9:14
    List<MaintenanceTimings> findByStartTimeLessThanAndEndTimeGreaterThan(
            LocalTime endTime,
            LocalTime startTime
    );
}
