package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.repository;

import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaReservationRepository extends JpaRepository<ReservationEntity, UUID> {

    List<ReservationEntity> findByRoomId(UUID roomId);

    List<ReservationEntity> findByUserId(UUID userId);
}