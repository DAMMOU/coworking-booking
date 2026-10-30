package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web;

import com.coworkinglab.coworkingbooking.domain.exception.CancellationNotAllowedException;
import com.coworkinglab.coworkingbooking.domain.exception.InvalidTimeSlotException;
import com.coworkinglab.coworkingbooking.domain.exception.SlotOverlapException;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTimeSlotException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidSlot(InvalidTimeSlotException ex) {
        return new ErrorResponse("INVALID_SLOT", ex.getMessage());
    }

    @ExceptionHandler(SlotOverlapException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOverlap(SlotOverlapException ex) {
        return new ErrorResponse("SLOT_OVERLAP", ex.getMessage());
    }

    @ExceptionHandler(CancellationNotAllowedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleCancellation(CancellationNotAllowedException ex) {
        return new ErrorResponse("CANCELLATION_NOT_ALLOWED", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(IllegalArgumentException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage());
    }
}