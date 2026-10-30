package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.port.in.CheckRoomAvailabilityUseCase;
import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.application.port.out.RoomRepository;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CheckRoomAvailabilityService implements CheckRoomAvailabilityUseCase {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public CheckRoomAvailabilityService(RoomRepository roomRepository,
                                        ReservationRepository reservationRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository);
        this.reservationRepository = Objects.requireNonNull(reservationRepository);
    }

    @Override
    public List<Room> checkAvailability(LocalDateTime from, LocalDateTime to) {
        TimeSlot requested = new TimeSlot(from, to);
        return roomRepository.findAll().stream()
                .filter(room -> isRoomFree(room, requested))
                .toList();
    }

    private boolean isRoomFree(Room room, TimeSlot requested) {
        List<Reservation> existing = reservationRepository.findByRoomId(room.id());
        return existing.stream().noneMatch(r -> r.slot().overlapsWith(requested));
    }
}