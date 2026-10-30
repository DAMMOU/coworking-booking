package com.coworkinglab.coworkingbooking.application.command;

import com.coworkinglab.coworkingbooking.domain.model.ReservationId;

import java.util.Objects;

public record CancelReservationCommand(ReservationId reservationId) {

    public CancelReservationCommand {
        Objects.requireNonNull(reservationId, "reservationId must not be null");
    }
}