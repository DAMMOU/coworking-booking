package com.coworkinglab.coworkingbooking.domain.model;

import com.coworkinglab.coworkingbooking.domain.exception.CancellationNotAllowedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    public static final Duration CANCELLATION_NOTICE = Duration.ofHours(24);

    private final ReservationId id;
    private final RoomId roomId;
    private final UserId userId;
    private final TimeSlot slot;
    private final LocalDateTime createdAt;
    private ReservationStatus status;

    private Reservation(ReservationId id,
                        RoomId roomId,
                        UserId userId,
                        TimeSlot slot,
                        LocalDateTime createdAt,
                        ReservationStatus status) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.roomId = Objects.requireNonNull(roomId, "roomId must not be null");
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.slot = Objects.requireNonNull(slot, "slot must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public static Reservation create(RoomId roomId, UserId userId, TimeSlot slot, LocalDateTime now) {
        Objects.requireNonNull(now, "now must not be null");
        if (slot.start().isBefore(now)) {
            throw new IllegalArgumentException("slot must not start in the past");
        }
        return new Reservation(
                ReservationId.newId(), roomId, userId, slot, now, ReservationStatus.CONFIRMED);
    }

    public static Reservation rehydrate(ReservationId id,
                                        RoomId roomId,
                                        UserId userId,
                                        TimeSlot slot,
                                        LocalDateTime createdAt,
                                        ReservationStatus status) {
        return new Reservation(id, roomId, userId, slot, createdAt, status);
    }

    public void cancel(LocalDateTime now) {
        Objects.requireNonNull(now, "now must not be null");
        if (status == ReservationStatus.CANCELLED) {
            throw new CancellationNotAllowedException("reservation is already cancelled");
        }
        Duration notice = Duration.between(now, slot.start());
        if (notice.compareTo(CANCELLATION_NOTICE) < 0) {
            throw new CancellationNotAllowedException(
                    "cancellation must be at least " + CANCELLATION_NOTICE.toHours() + " hours before start");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public ReservationId id() { return id; }
    public RoomId roomId() { return roomId; }
    public UserId userId() { return userId; }
    public TimeSlot slot() { return slot; }
    public LocalDateTime createdAt() { return createdAt; }
    public ReservationStatus status() { return status; }
}