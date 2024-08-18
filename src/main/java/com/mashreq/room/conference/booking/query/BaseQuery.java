package com.mashreq.room.conference.booking.query;

public interface BaseQuery<C extends Query, R> {
    R handle(C query);
}
