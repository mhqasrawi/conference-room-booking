package com.mashreq.room.conference.booking.exceptions;

public class BookingError {

    public static final ErrorCode ROOM_NOT_FOUND = new ErrorCode("10001", "Room not found");
    public static final ErrorCode ROOM_ALREADY_EXISTS = new ErrorCode("10002", "Room already exists");
    public static final ErrorCode SOMETHING_WENT_WRONG = new ErrorCode("10003", "Something went wrong");

    public static final ErrorCode MAINTENANCE_TIMINGS_NOT_FOUND = new ErrorCode("10004", "Maintenance timings not found");
    public static final ErrorCode ROOM_DISABLED = new ErrorCode("10005", "Room is disabled");
    public static final ErrorCode ROOM_NOT_AVAILABLE = new ErrorCode("10006", "No Rooms Available with requested capacity");
    public static final ErrorCode FULLY_BOOKED_AT_GIVEN_TIME = new ErrorCode("10007", "Rooms is fully booked at given" +
            " time , please adjust your start/end time");
    public static final ErrorCode BOOKING_NOT_FOUND = new ErrorCode("10008", "Booking not found");
    public static final ErrorCode MAINTENANCE_TIME_OVERLAP = new ErrorCode("10009", "Requested time overlaps with maintenance time");
    public static final ErrorCode TOKEN_EXPIRED = new ErrorCode("10010", "Token expired");
}
