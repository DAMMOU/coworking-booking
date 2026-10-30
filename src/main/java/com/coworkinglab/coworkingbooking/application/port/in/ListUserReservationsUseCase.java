package com.coworkinglab.coworkingbooking.application.port.in;

import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.UserId;

import java.util.List;

public interface ListUserReservationsUseCase {

    List<Reservation> listForUser(UserId userId);
}