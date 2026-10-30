package com.coworkinglab.coworkingbooking.infrastructure.config;

import com.coworkinglab.coworkingbooking.application.port.in.BookRoomUseCase;
import com.coworkinglab.coworkingbooking.application.port.in.CancelReservationUseCase;
import com.coworkinglab.coworkingbooking.application.port.in.CheckRoomAvailabilityUseCase;
import com.coworkinglab.coworkingbooking.application.port.in.ListUserReservationsUseCase;
import com.coworkinglab.coworkingbooking.application.port.out.NotificationSender;
import com.coworkinglab.coworkingbooking.application.port.out.ReservationRepository;
import com.coworkinglab.coworkingbooking.application.port.out.RoomRepository;
import com.coworkinglab.coworkingbooking.application.service.BookRoomService;
import com.coworkinglab.coworkingbooking.application.service.CancelReservationService;
import com.coworkinglab.coworkingbooking.application.service.CheckRoomAvailabilityService;
import com.coworkinglab.coworkingbooking.application.service.ListUserReservationsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BeanConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public CheckRoomAvailabilityUseCase checkRoomAvailabilityUseCase(
            RoomRepository roomRepository,
            ReservationRepository reservationRepository) {
        return new CheckRoomAvailabilityService(roomRepository, reservationRepository);
    }

    @Bean
    public BookRoomUseCase bookRoomUseCase(
            RoomRepository roomRepository,
            ReservationRepository reservationRepository,
            NotificationSender notificationSender,
            Clock clock) {
        return new BookRoomService(roomRepository, reservationRepository, notificationSender, clock);
    }

    @Bean
    public CancelReservationUseCase cancelReservationUseCase(
            ReservationRepository reservationRepository,
            NotificationSender notificationSender,
            Clock clock) {
        return new CancelReservationService(reservationRepository, notificationSender, clock);
    }

    @Bean
    public ListUserReservationsUseCase listUserReservationsUseCase(
            ReservationRepository reservationRepository) {
        return new ListUserReservationsService(reservationRepository);
    }
}