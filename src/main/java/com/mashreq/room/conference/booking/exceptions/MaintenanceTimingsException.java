package com.mashreq.room.conference.booking.exceptions;

import lombok.Getter;

@Getter
public class MaintenanceTimingsException extends RuntimeException {

    private ErrorCode errorCode;

    public MaintenanceTimingsException(String message) {
        super(message);
    }

    public MaintenanceTimingsException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
