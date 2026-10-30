package com.coworkinglab.coworkingbooking.application.port.in;

import com.coworkinglab.coworkingbooking.application.command.CancelReservationCommand;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;

public interface CancelReservationUseCase {

    Reservation cancel(CancelReservationCommand command);
}