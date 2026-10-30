package com.coworkinglab.coworkingbooking.application.fake;

import com.coworkinglab.coworkingbooking.application.port.out.NotificationSender;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class FakeNotificationSender implements NotificationSender {

    private final List<Reservation> confirmations = new ArrayList<>();
    private final List<Reservation> cancellations = new ArrayList<>();

    @Override
    public void sendReservationConfirmation(Reservation reservation) {
        confirmations.add(reservation);
    }

    @Override
    public void sendReservationCancellation(Reservation reservation) {
        cancellations.add(reservation);
    }

    public List<Reservation> confirmations() {
        return List.copyOf(confirmations);
    }

    public List<Reservation> cancellations() {
        return List.copyOf(cancellations);
    }
}