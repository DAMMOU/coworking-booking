package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.mapper;

import com.coworkinglab.coworkingbooking.application.command.BookRoomCommand;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.BookRoomRequest;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.ReservationResponse;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.RoomResponse;
import org.springframework.stereotype.Component;

@Component
public class WebMapper {

    public RoomResponse toResponse(Room room) {
        return new RoomResponse(room.id().value(), room.name(), room.capacity());
    }

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.id().value(),
                reservation.roomId().value(),
                reservation.userId().value(),
                reservation.slot().start(),
                reservation.slot().end(),
                reservation.status().name(),
                reservation.createdAt());
    }

    public BookRoomCommand toCommand(BookRoomRequest request) {
        return new BookRoomCommand(
                new RoomId(request.roomId()),
                new UserId(request.userId()),
                request.start(),
                request.end());
    }
}