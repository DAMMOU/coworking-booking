package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.repository;

import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaRoomRepository extends JpaRepository<RoomEntity, UUID> {
}