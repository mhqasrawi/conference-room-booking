package com.mashreq.room.conference.booking.runner;

import com.mashreq.room.conference.booking.entities.MaintenanceTimings;
import com.mashreq.room.conference.booking.entities.Room;
import com.mashreq.room.conference.booking.repositories.MaintenanceTimingRepository;
import com.mashreq.room.conference.booking.repositories.RoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DataInitializer.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MaintenanceTimingRepository maintenanceTimingRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing data...");

        try {
            roomRepository.save(new Room("Amaze", 3, false));
            roomRepository.save(new Room("Beauty", 7, false));
            roomRepository.save(new Room("Inspire", 12, false));
            roomRepository.save(new Room("Strive", 20, false));


            maintenanceTimingRepository.save(new MaintenanceTimings(LocalTime.parse("09:00"),
                    LocalTime.parse("09:15")));
            maintenanceTimingRepository.save(new MaintenanceTimings(LocalTime.parse("13:00"),
                    LocalTime.parse("13:15")));
            maintenanceTimingRepository.save(new MaintenanceTimings(LocalTime.parse("17:00"),
                    LocalTime.parse( "17:15")));
            logger.info("Data initialized successfully");
        } catch (Exception e) {
            logger.error("Error while initializing data: " + e.getMessage());
        }


    }
}
