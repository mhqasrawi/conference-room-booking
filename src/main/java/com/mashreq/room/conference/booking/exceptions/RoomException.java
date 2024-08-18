package com.mashreq.room.conference.booking.exceptions;

import lombok.Getter;

@Getter
public class RoomException extends RuntimeException {

    private ErrorCode code;

    public RoomException(String message) {
        super(message);
    }

    public RoomException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
