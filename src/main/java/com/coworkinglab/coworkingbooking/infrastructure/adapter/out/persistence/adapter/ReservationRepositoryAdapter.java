package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.adapter;

import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.ReservationId;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.repository.JpaReservationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final JpaReservationRepository jpa;
    private final PersistenceMapper mapper;

    public ReservationRepositoryAdapter(JpaReservationRepository jpa, PersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity saved = jpa.save(mapper.toEntity(reservation));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(ReservationId id) {
        return jpa.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Reservation> findByRoomId(RoomId roomId) {
        return jpa.findByRoomId(roomId.value()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Reservation> findByUserId(UserId userId) {
        return jpa.findByUserId(userId.value()).stream().map(mapper::toDomain).toList();
    }
}