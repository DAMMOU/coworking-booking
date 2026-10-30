package com.coworkinglab.coworkingbooking.application.fake;

import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.ReservationId;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.UserId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<ReservationId, Reservation> reservations = new HashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(ReservationId id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findByRoomId(RoomId roomId) {
        return reservations.values().stream()
                .filter(r -> r.roomId().equals(roomId))
                .toList();
    }

    @Override
    public List<Reservation> findByUserId(UserId userId) {
        return reservations.values().stream()
                .filter(r -> r.userId().equals(userId))
                .toList();
    }
}