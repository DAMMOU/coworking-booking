package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.command.BookRoomCommand;
import com.coworkinglab.coworkingbooking.application.fake.FakeNotificationSender;
import com.coworkinglab.coworkingbooking.application.fake.InMemoryReservationRepository;
import com.coworkinglab.coworkingbooking.application.fake.InMemoryRoomRepository;
import com.coworkinglab.coworkingbooking.domain.exception.SlotOverlapException;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.ReservationStatus;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookRoomServiceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 10, 10, 0);
    private static final Clock FIXED_CLOCK =
            Clock.fixed(NOW.atZone(ZONE).toInstant(), ZONE);

    private InMemoryRoomRepository roomRepository;
    private InMemoryReservationRepository reservationRepository;
    private FakeNotificationSender notificationSender;
    private BookRoomService service;

    private RoomId roomId;
    private UserId userId;

    @BeforeEach
    void setUp() {
        roomRepository = new InMemoryRoomRepository();
        reservationRepository = new InMemoryReservationRepository();
        notificationSender = new FakeNotificationSender();
        service = new BookRoomService(
                roomRepository, reservationRepository, notificationSender, FIXED_CLOCK);

        roomId = RoomId.newId();
        userId = UserId.newId();
        roomRepository.add(new Room(roomId, "Salle Atlas", 8));
    }

    @Test
    void should_book_room_successfully() {
        BookRoomCommand command = new BookRoomCommand(
                roomId, userId,
                LocalDateTime.of(2026, 6, 15, 14, 0),
                LocalDateTime.of(2026, 6, 15, 16, 0));

        Reservation reservation = service.book(command);

        assertThat(reservation.id()).isNotNull();
        assertThat(reservation.roomId()).isEqualTo(roomId);
        assertThat(reservation.userId()).isEqualTo(userId);
        assertThat(reservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(reservationRepository.findById(reservation.id())).isPresent();
        assertThat(notificationSender.confirmations()).hasSize(1);
    }

    @Test
    void should_reject_when_room_not_found() {
        BookRoomCommand command = new BookRoomCommand(
                RoomId.newId(), userId,
                LocalDateTime.of(2026, 6, 15, 14, 0),
                LocalDateTime.of(2026, 6, 15, 16, 0));

        assertThatThrownBy(() -> service.book(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("room not found");
    }

    @Test
    void should_reject_when_slot_overlaps_existing_reservation() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 15, 14, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 15, 16, 0);
        service.book(new BookRoomCommand(roomId, userId, start, end));

        BookRoomCommand overlapping = new BookRoomCommand(
                roomId, UserId.newId(),
                start.plusHours(1), end.plusHours(1));

        assertThatThrownBy(() -> service.book(overlapping))
                .isInstanceOf(SlotOverlapException.class);
    }

    @Test
    void should_reject_slot_in_the_past() {
        BookRoomCommand command = new BookRoomCommand(
                roomId, userId,
                NOW.minusHours(2),
                NOW.minusHours(1));

        assertThatThrownBy(() -> service.book(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("past");
    }

    @Test
    void should_not_send_notification_when_booking_fails() {
        BookRoomCommand command = new BookRoomCommand(
                RoomId.newId(), userId,
                LocalDateTime.of(2026, 6, 15, 14, 0),
                LocalDateTime.of(2026, 6, 15, 16, 0));

        try {
            service.book(command);
        } catch (IllegalArgumentException ignored) {
        }

        assertThat(notificationSender.confirmations()).isEmpty();
    }
}