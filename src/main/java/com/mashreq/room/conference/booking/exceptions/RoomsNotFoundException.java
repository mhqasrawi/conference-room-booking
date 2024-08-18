package com.mashreq.room.conference.booking.exceptions;

import lombok.Getter;

@Getter
public class RoomsNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public RoomsNotFoundException(String message) {
        super(message);
    }

    public RoomsNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
