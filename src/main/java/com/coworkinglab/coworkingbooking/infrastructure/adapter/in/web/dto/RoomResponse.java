package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record RoomResponse(UUID id, String name, int capacity) {
}