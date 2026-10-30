package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.mapper;

import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.ReservationId;
import com.coworkinglab.coworkingbooking.domain.model.ReservationStatus;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.TimeSlot;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity.ReservationEntity;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.entity.RoomEntity;
import org.springframework.stereotype.Component;

@Component
public class PersistenceMapper {

    public Room toDomain(RoomEntity entity) {
        return new Room(
                new RoomId(entity.getId()),
                entity.getName(),
                entity.getCapacity());
    }

    public RoomEntity toEntity(Room room) {
        return new RoomEntity(
                room.id().value(),
                room.name(),
                room.capacity());
    }

    public Reservation toDomain(ReservationEntity entity) {
        TimeSlot slot = new TimeSlot(entity.getStartTime(), entity.getEndTime());
        return Reservation.rehydrate(
                new ReservationId(entity.getId()),
                new RoomId(entity.getRoomId()),
                new UserId(entity.getUserId()),
                slot,
                entity.getCreatedAt(),
                ReservationStatus.valueOf(entity.getStatus()));
    }

    public ReservationEntity toEntity(Reservation reservation) {
        return new ReservationEntity(
                reservation.id().value(),
                reservation.roomId().value(),
                reservation.userId().value(),
                reservation.slot().start(),
                reservation.slot().end(),
                reservation.status().name(),
                reservation.createdAt());
    }
}