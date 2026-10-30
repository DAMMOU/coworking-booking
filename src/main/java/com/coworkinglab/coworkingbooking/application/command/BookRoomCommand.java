package com.coworkinglab.coworkingbooking.application.command;

import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

public record BookRoomCommand(
        RoomId roomId,
        UserId userId,
        LocalDateTime start,
        LocalDateTime end) {

    public BookRoomCommand {
        Objects.requireNonNull(roomId, "roomId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
    }
}