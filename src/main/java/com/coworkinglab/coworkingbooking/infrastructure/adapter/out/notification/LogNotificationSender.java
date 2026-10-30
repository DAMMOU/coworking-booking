package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.notification;

import com.coworkinglab.coworkingbooking.application.port.out.NotificationSender;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(LogNotificationSender.class);

    @Override
    public void sendReservationConfirmation(Reservation reservation) {
        log.info("Reservation confirmed: id={}, room={}, user={}, slot={}",
                reservation.id(), reservation.roomId(), reservation.userId(), reservation.slot());
    }

    @Override
    public void sendReservationCancellation(Reservation reservation) {
        log.info("Reservation cancelled: id={}, room={}, user={}, slot={}",
                reservation.id(), reservation.roomId(), reservation.userId(), reservation.slot());
    }
}