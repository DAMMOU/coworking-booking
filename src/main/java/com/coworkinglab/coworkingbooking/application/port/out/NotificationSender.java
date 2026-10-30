package com.coworkinglab.coworkingbooking.application.port.out;

import com.coworkinglab.coworkingbooking.domain.model.Reservation;

public interface NotificationSender {

    void sendReservationConfirmation(Reservation reservation);

    void sendReservationCancellation(Reservation reservation);
}