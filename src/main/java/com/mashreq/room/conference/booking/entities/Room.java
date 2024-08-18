package com.mashreq.room.conference.booking.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Room")
@Table(
        name = "Room",
        indexes = @Index(name = "capacity_asc", columnList = "capacity ASC")
)
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private boolean isDisabled = false;

    public Room(String name, int capacity, boolean isDisabled) {
        this.name = name;
        this.capacity = capacity;
    }
}
