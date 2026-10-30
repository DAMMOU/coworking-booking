package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    private UUID id;

    private UUID roomId;

    private UUID userId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

    private LocalDateTime createdAt;

    protected ReservationEntity() {
    }

    public ReservationEntity(UUID id,
                             UUID roomId,
                             UUID userId,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             String status,
                             LocalDateTime createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getRoomId() { return roomId; }
    public UUID getUserId() { return userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}