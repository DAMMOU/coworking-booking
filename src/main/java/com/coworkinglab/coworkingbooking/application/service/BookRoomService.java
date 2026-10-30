package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.command.BookRoomCommand;
import com.coworkinglab.coworkingbooking.application.port.in.BookRoomUseCase;
import com.coworkinglab.coworkingbooking.application.port.out.NotificationSender;
import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.application.port.out.RoomRepository;
import com.coworkinglab.coworkingbooking.domain.exception.SlotOverlapException;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.TimeSlot;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BookRoomService implements BookRoomUseCase {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationSender notificationSender;
    private final Clock clock;

    public BookRoomService(RoomRepository roomRepository,
                           ReservationRepository reservationRepository,
                           NotificationSender notificationSender,
                           Clock clock) {
        this.roomRepository = Objects.requireNonNull(roomRepository);
        this.reservationRepository = Objects.requireNonNull(reservationRepository);
        this.notificationSender = Objects.requireNonNull(notificationSender);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public Reservation book(BookRoomCommand command) {
        Room room = roomRepository.findById(command.roomId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "room not found: " + command.roomId()));

        TimeSlot slot = new TimeSlot(command.start(), command.end());

        ensureRoomIsFree(room, slot);

        LocalDateTime now = LocalDateTime.now(clock);
        Reservation reservation = Reservation.create(room.id(), command.userId(), slot, now);
        Reservation saved = reservationRepository.save(reservation);

        notificationSender.sendReservationConfirmation(saved);

        return saved;
    }

    private void ensureRoomIsFree(Room room, TimeSlot slot) {
        List<Reservation> existing = reservationRepository.findByRoomId(room.id());
        boolean overlap = existing.stream()
                .anyMatch(r -> r.slot().overlapsWith(slot));
        if (overlap) {
            throw new SlotOverlapException(
                    "room " + room.id() + " is already booked on slot " + slot);
        }
    }
}