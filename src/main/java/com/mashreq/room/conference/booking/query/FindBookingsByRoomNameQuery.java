package com.mashreq.room.conference.booking.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FindBookingsByRoomNameQuery implements Query{
    private String roomName;
}
