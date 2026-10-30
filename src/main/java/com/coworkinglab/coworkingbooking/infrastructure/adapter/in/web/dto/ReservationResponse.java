package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID roomId,
        UUID userId,
        LocalDateTime start,
        LocalDateTime end,
        String status,
        LocalDateTime createdAt) {
}