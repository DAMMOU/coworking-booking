package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.command.CancelReservationCommand;
import com.coworkinglab.coworkingbooking.application.fake.FakeNotificationSender;
import com.coworkinglab.coworkingbooking.application.fake.InMemoryReservationRepository;
import com.coworkinglab.coworkingbooking.domain.exception.CancellationNotAllowedException;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.ReservationStatus;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.TimeSlot;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CancelReservationServiceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 10, 10, 0);
    private static final Clock FIXED_CLOCK =
            Clock.fixed(NOW.atZone(ZONE).toInstant(), ZONE);

    private InMemoryReservationRepository reservationRepository;
    private FakeNotificationSender notificationSender;
    private CancelReservationService service;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        notificationSender = new FakeNotificationSender();
        service = new CancelReservationService(
                reservationRepository, notificationSender, FIXED_CLOCK);
    }

    @Test
    void should_cancel_reservation_successfully() {
        Reservation reservation = createReservation(LocalDateTime.of(2026, 6, 15, 14, 0));
        reservationRepository.save(reservation);

        Reservation cancelled = service.cancel(
                new CancelReservationCommand(reservation.id()));

        assertThat(cancelled.status()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(notificationSender.cancellations()).hasSize(1);
    }

    @Test
    void should_reject_when_reservation_not_found() {
        assertThatThrownBy(() -> service.cancel(
                new CancelReservationCommand(
                        com.coworkinglab.coworkingbooking.domain.model.ReservationId.newId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("reservation not found");
    }

    @Test
    void should_reject_cancellation_too_late() {
        Reservation reservation = createReservation(NOW.plusHours(12));
        reservationRepository.save(reservation);

        assertThatThrownBy(() -> service.cancel(
                new CancelReservationCommand(reservation.id())))
                .isInstanceOf(CancellationNotAllowedException.class);
    }

    private Reservation createReservation(LocalDateTime start) {
        TimeSlot slot = new TimeSlot(start, start.plusHours(2));
        return Reservation.create(RoomId.newId(), UserId.newId(), slot, NOW);
    }
}