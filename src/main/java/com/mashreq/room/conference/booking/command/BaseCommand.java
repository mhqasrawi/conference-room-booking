package com.mashreq.room.conference.booking.command;

public interface BaseCommand<C extends Command, R> {
    R handle(C command);

}
