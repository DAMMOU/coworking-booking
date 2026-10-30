package com.coworkinglab.coworkingbooking.domain.exception;

public class CancellationNotAllowedException extends DomainException {
    public CancellationNotAllowedException(String message) {
        super(message);
    }
}