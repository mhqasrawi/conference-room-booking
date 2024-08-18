package com.mashreq.room.conference.booking.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAvailableRoomsByTimeRangeQuery implements Query{

    private String startTime;
    private String endTime;
}
