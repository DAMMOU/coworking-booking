package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    private UUID id;

    private String name;

    private int capacity;

    protected RoomEntity() {
    }

    public RoomEntity(UUID id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
}