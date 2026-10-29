package com.coworkinglab.coworkingbooking.domain.model;

import java.util.Objects;
import java.util.UUID;

public record RoomId(UUID value) {

    public RoomId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static RoomId newId() {
        return new RoomId(UUID.randomUUID());
    }

    public static RoomId of(String value) {
        return new RoomId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}