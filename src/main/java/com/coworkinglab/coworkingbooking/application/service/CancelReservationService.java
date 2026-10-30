package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.command.CancelReservationCommand;
import com.coworkinglab.coworkingbooking.application.port.in.CancelReservationUseCase;
import com.coworkinglab.coworkingbooking.application.port.out.NotificationSender;
import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public class CancelReservationService implements CancelReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final NotificationSender notificationSender;
    private final Clock clock;

    public CancelReservationService(ReservationRepository reservationRepository,
                                    NotificationSender notificationSender,
                                    Clock clock) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository);
        this.notificationSender = Objects.requireNonNull(notificationSender);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public Reservation cancel(CancelReservationCommand command) {
        Reservation reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "reservation not found: " + command.reservationId()));

        LocalDateTime now = LocalDateTime.now(clock);
        reservation.cancel(now);
        Reservation saved = reservationRepository.save(reservation);
        notificationSender.sendReservationCancellation(saved);

        return saved;
    }
}