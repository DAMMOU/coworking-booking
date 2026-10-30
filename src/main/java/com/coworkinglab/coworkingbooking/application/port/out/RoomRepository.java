package com.coworkinglab.coworkingbooking.application.port.out;

import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(RoomId id);

    List<Room> findAll();
}