package com.coworkinglab.coworkingbooking.domain.model;

import com.coworkinglab.coworkingbooking.domain.exception.CancellationNotAllowedException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 10, 10, 0);
    private static final LocalDateTime SLOT_START = LocalDateTime.of(2026, 6, 15, 14, 0);
    private static final LocalDateTime SLOT_END = LocalDateTime.of(2026, 6, 15, 16, 0);

    private static final RoomId ROOM_ID = RoomId.newId();
    private static final UserId USER_ID = UserId.newId();

    private static TimeSlot validSlot() {
        return new TimeSlot(SLOT_START, SLOT_END);
    }

    // --- Création ---

    @Test
    void should_create_confirmed_reservation() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);

        assertThat(reservation.id()).isNotNull();
        assertThat(reservation.roomId()).isEqualTo(ROOM_ID);
        assertThat(reservation.userId()).isEqualTo(USER_ID);
        assertThat(reservation.slot()).isEqualTo(validSlot());
        assertThat(reservation.createdAt()).isEqualTo(NOW);
        assertThat(reservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    void should_reject_slot_starting_in_the_past() {
        TimeSlot pastSlot = new TimeSlot(
                NOW.minusHours(1), NOW.plusHours(1));

        assertThatThrownBy(() -> Reservation.create(ROOM_ID, USER_ID, pastSlot, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("past");
    }

    @Test
    void should_reject_null_room_id() {
        assertThatThrownBy(() -> Reservation.create(null, USER_ID, validSlot(), NOW))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_null_slot() {
        assertThatThrownBy(() -> Reservation.create(ROOM_ID, USER_ID, null, NOW))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_null_now() {
        assertThatThrownBy(() -> Reservation.create(ROOM_ID, USER_ID, validSlot(), null))
                .isInstanceOf(NullPointerException.class);
    }

    // --- Annulation ---

    @Test
    void should_cancel_when_more_than_24h_before_start() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);
        LocalDateTime cancellationTime = SLOT_START.minusHours(48);

        reservation.cancel(cancellationTime);

        assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void should_cancel_exactly_24h_before_start() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);
        LocalDateTime cancellationTime = SLOT_START.minusHours(24);

        reservation.cancel(cancellationTime);

        assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void should_reject_cancellation_less_than_24h_before_start() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);
        LocalDateTime cancellationTime = SLOT_START.minusHours(23);

        assertThatThrownBy(() -> reservation.cancel(cancellationTime))
                .isInstanceOf(CancellationNotAllowedException.class)
                .hasMessageContaining("24 hours");
    }

    @Test
    void should_reject_cancellation_after_start() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);
        LocalDateTime cancellationTime = SLOT_START.plusHours(1);

        assertThatThrownBy(() -> reservation.cancel(cancellationTime))
                .isInstanceOf(CancellationNotAllowedException.class);
    }

    @Test
    void should_reject_double_cancellation() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);
        LocalDateTime cancellationTime = SLOT_START.minusHours(48);
        reservation.cancel(cancellationTime);

        assertThatThrownBy(() -> reservation.cancel(cancellationTime))
                .isInstanceOf(CancellationNotAllowedException.class)
                .hasMessageContaining("already cancelled");
    }

    @Test
    void should_reject_null_cancellation_time() {
        Reservation reservation = Reservation.create(ROOM_ID, USER_ID, validSlot(), NOW);

        assertThatThrownBy(() -> reservation.cancel(null))
                .isInstanceOf(NullPointerException.class);
    }

    // --- Rehydrate ---

    @Test
    void should_rehydrate_reservation_from_persistence() {
        ReservationId id = ReservationId.newId();
        ReservationStatus status = ReservationStatus.CANCELLED;

        Reservation reservation = Reservation.rehydrate(
                id, ROOM_ID, USER_ID, validSlot(), NOW, status);

        assertThat(reservation.id()).isEqualTo(id);
        assertThat(reservation.roomId()).isEqualTo(ROOM_ID);
        assertThat(reservation.userId()).isEqualTo(USER_ID);
        assertThat(reservation.slot()).isEqualTo(validSlot());
        assertThat(reservation.createdAt()).isEqualTo(NOW);
        assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void should_reject_null_id_in_rehydrate() {
        assertThatThrownBy(() -> Reservation.rehydrate(
                null, ROOM_ID, USER_ID, validSlot(), NOW, ReservationStatus.CONFIRMED))
                .isInstanceOf(NullPointerException.class);
    }
}