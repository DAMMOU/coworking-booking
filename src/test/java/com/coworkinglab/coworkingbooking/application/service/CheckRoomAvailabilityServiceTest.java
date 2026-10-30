package com.coworkinglab.coworkingbooking.application.service;

import com.coworkinglab.coworkingbooking.application.fake.InMemoryReservationRepository;
import com.coworkinglab.coworkingbooking.application.fake.InMemoryRoomRepository;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.domain.model.TimeSlot;
import com.coworkinglab.coworkingbooking.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CheckRoomAvailabilityServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 10, 10, 0);
    private static final LocalDateTime FROM = LocalDateTime.of(2026, 6, 15, 14, 0);
    private static final LocalDateTime TO = LocalDateTime.of(2026, 6, 15, 16, 0);

    private InMemoryRoomRepository roomRepository;
    private InMemoryReservationRepository reservationRepository;
    private CheckRoomAvailabilityService service;

    private RoomId roomA;
    private RoomId roomB;

    @BeforeEach
    void setUp() {
        roomRepository = new InMemoryRoomRepository();
        reservationRepository = new InMemoryReservationRepository();
        service = new CheckRoomAvailabilityService(roomRepository, reservationRepository);

        roomA = RoomId.newId();
        roomB = RoomId.newId();
        roomRepository.add(new Room(roomA, "Salle Atlas", 8));
        roomRepository.add(new Room(roomB, "Salle Yasmine", 4));
    }


    @Test
    void should_return_all_rooms_when_no_reservation() {
        List<Room> available = service.checkAvailability(FROM, TO);

        assertThat(available).hasSize(2);
    }

    @Test
    void should_exclude_room_with_overlapping_reservation() {
        Reservation reservation = Reservation.create(
                roomA, UserId.newId(), new TimeSlot(FROM, TO), NOW);
        reservationRepository.save(reservation);

        List<Room> available = service.checkAvailability(FROM, TO);

        assertThat(available).hasSize(1);
        assertThat(available.get(0).id()).isEqualTo(roomB);
    }

    @Test
    void should_include_room_when_reservation_is_adjacent() {
        Reservation reservation = Reservation.create(
                roomA, UserId.newId(), new TimeSlot(TO, TO.plusHours(2)), NOW);
        reservationRepository.save(reservation);

        List<Room> available = service.checkAvailability(FROM, TO);

        assertThat(available).hasSize(2);
    }
}