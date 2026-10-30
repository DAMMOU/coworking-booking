package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.port.in.ListUserReservationsUseCase;
import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.UserId;

import java.util.List;
import java.util.Objects;

public class ListUserReservationsService implements ListUserReservationsUseCase {

    private final ReservationRepository reservationRepository;

    public ListUserReservationsService(ReservationRepository reservationRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository);
    }

    @Override
    public List<Reservation> listForUser(UserId userId) {
        return reservationRepository.findByUserId(userId);
    }
}