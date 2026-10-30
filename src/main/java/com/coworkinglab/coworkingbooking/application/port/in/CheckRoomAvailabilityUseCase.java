package com.coworkinglab.coworkingbooking.application.port.in;

import com.coworkinglab.coworkingbooking.domain.model.Room;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckRoomAvailabilityUseCase {

    List<Room> checkAvailability(LocalDateTime from, LocalDateTime to);
}