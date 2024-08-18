package com.mashreq.room.conference.booking.repositories;

import com.mashreq.room.conference.booking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByDateAndStartTimeLessThanAndEndTimeGreaterThanAndRoomNameIn(LocalDate date, LocalTime endTime,
                                                                                   LocalTime startTime, List<String> roomNames);

    @Query("SELECT b FROM Booking b WHERE LOWER(b.bookedBy) = LOWER(:bookedBy)")
    List<Booking>findByBookedBy(String bookedBy);

    List<Booking>findByDate(LocalDate date);


    @Query("SELECT b FROM Booking b WHERE LOWER(b.roomName) = LOWER(:roomName)")
    List<Booking> findByRoomName(String roomName);

    List<Booking> findByRoomNameAndStartTimeLessThanAndEndTimeGreaterThan(String name, LocalTime endTime, LocalTime startTime);
}