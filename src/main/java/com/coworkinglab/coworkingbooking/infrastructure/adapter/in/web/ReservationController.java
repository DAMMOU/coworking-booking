package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web;

import com.coworkinglab.coworkingbooking.application.command.CancelReservationCommand;
import com.coworkinglab.coworkingbooking.application.port.in.BookRoomUseCase;
import com.coworkinglab.coworkingbooking.application.port.in.CancelReservationUseCase;
import com.coworkinglab.coworkingbooking.application.port.in.ListUserReservationsUseCase;
import com.coworkinglab.coworkingbooking.domain.model.ReservationId;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.BookRoomRequest;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.ReservationResponse;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.mapper.WebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final BookRoomUseCase bookRoom;
    private final CancelReservationUseCase cancelReservation;
    private final ListUserReservationsUseCase listUserReservations;
    private final WebMapper mapper;

    public ReservationController(BookRoomUseCase bookRoom,
                                 CancelReservationUseCase cancelReservation,
                                 ListUserReservationsUseCase listUserReservations,
                                 WebMapper mapper) {
        this.bookRoom = bookRoom;
        this.cancelReservation = cancelReservation;
        this.listUserReservations = listUserReservations;
        this.mapper = mapper;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> book(@RequestBody BookRoomRequest request) {
        ReservationResponse response = mapper.toResponse(bookRoom.book(mapper.toCommand(request)));
        return ResponseEntity.created(URI.create("/api/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ReservationResponse cancel(@PathVariable UUID id) {
        return mapper.toResponse(
                cancelReservation.cancel(new CancelReservationCommand(new ReservationId(id))));
    }

    @GetMapping("/users/{userId}/reservations")
    public List<ReservationResponse> listForUser(@PathVariable UUID userId) {
        return listUserReservations.listForUser(new UserId(userId)).stream()
                .map(mapper::toResponse)
                .toList();
    }
}