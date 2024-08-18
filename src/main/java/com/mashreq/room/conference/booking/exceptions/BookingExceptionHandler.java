package com.mashreq.room.conference.booking.exceptions;

import com.mashreq.room.conference.booking.query.handler.FindAllBookingsHandler;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookingExceptionHandler {
    private static final Logger logger = LogManager.getLogger(BookingExceptionHandler.class);
    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ErrorResponse> handleRoomException(RoomException ex) {
        ErrorCode code = ex.getCode();
        if (code.getCode().equalsIgnoreCase(BookingError.ROOM_NOT_FOUND.getCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(code.getCode(), code.getMessage()));
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(code.getCode());
            errorResponse.setMessage(code.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBookingException(BookingException ex) {
        ErrorCode code = ex.getErrorCode();
        if (code.getCode().equalsIgnoreCase(BookingError.BOOKING_NOT_FOUND.getCode()) || code.getCode().equalsIgnoreCase(BookingError.ROOM_NOT_FOUND.getCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(code.getCode(), code.getMessage()));
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(code.getCode());
            errorResponse.setMessage(code.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }





    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode("500");
        errorResponse.setMessage("An unexpected error occurred.");
        logger.error("An unexpected error occurred: ", ex);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(MaintenanceTimingsException.class)
    public ResponseEntity<ErrorResponse> handleMaintenanceTimingsNotFoundException(MaintenanceTimingsException ex) {
        ErrorCode code = ex.getErrorCode();
        if (code.getCode().equalsIgnoreCase(BookingError.MAINTENANCE_TIMINGS_NOT_FOUND.getCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(code.getCode(), code.getMessage()));
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(code.getCode());
            errorResponse.setMessage(code.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCode> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errorMessage.append(error.getDefaultMessage()).append(" ")
        );
        return ResponseEntity.badRequest().body(new ErrorCode("000000", errorMessage.toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCode> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        // Return constraint violation messages
        return ResponseEntity.badRequest().body(new ErrorCode("000000", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorCode code = new ErrorCode("401", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(code.getCode(), code.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorCode code = new ErrorCode("401", "Bad credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(code.getCode(), code.getMessage()));
    }
}