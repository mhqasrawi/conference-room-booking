package com.mashreq.room.conference.booking.repositories;

import com.mashreq.room.conference.booking.entities.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByCapacityGreaterThanEqualOrderByCapacityAsc(int capacity);



    @Query("SELECT r FROM Room r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<Room> findByName(@Param("name") String name);


}
