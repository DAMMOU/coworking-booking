package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookRoomRequest(
        UUID roomId,
        UUID userId,
        LocalDateTime start,
        LocalDateTime end) {
}