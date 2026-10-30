package com.coworkinglab.coworkingbooking.application.fake;

import com.coworkinglab.coworkingbooking.application.port.out.RoomRepository;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRoomRepository implements RoomRepository {

    private final Map<RoomId, Room> rooms = new HashMap<>();

    @Override
    public Optional<Room> findById(RoomId id) {
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public List<Room> findAll() {
        return List.copyOf(rooms.values());
    }

    public void add(Room room) {
        rooms.put(room.id(), room);
    }
}