package com.coworkinglab.coworkingbooking.domain.model;

import java.util.Objects;

public class Room {

    private final RoomId id;
    private final String name;
    private final int capacity;

    public Room(RoomId id, String name, int capacity) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = requireNonBlank(name, "name");
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    public RoomId id() { return id; }
    public String name() { return name; }
    public int capacity() { return capacity; }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}