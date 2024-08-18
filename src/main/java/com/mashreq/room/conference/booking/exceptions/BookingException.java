package com.mashreq.room.conference.booking.exceptions;

import lombok.Getter;

@Getter
public class BookingException extends RuntimeException {

    private ErrorCode errorCode;
    public BookingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode=errorCode;
    }
}
