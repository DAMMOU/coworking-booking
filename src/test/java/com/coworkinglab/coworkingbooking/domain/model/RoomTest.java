package com.coworkinglab.coworkingbooking.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomTest {

    private static final RoomId ROOM_ID = RoomId.newId();

    @Test
    void should_create_valid_room() {
        Room room = new Room(ROOM_ID, "Salle Atlas", 8);

        assertThat(room.id()).isEqualTo(ROOM_ID);
        assertThat(room.name()).isEqualTo("Salle Atlas");
        assertThat(room.capacity()).isEqualTo(8);
    }

    @Test
    void should_reject_null_id() {
        assertThatThrownBy(() -> new Room(null, "Salle Atlas", 8))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_null_name() {
        assertThatThrownBy(() -> new Room(ROOM_ID, null, 8))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }

    @Test
    void should_reject_blank_name() {
        assertThatThrownBy(() -> new Room(ROOM_ID, "   ", 8))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }

    @Test
    void should_reject_zero_capacity() {
        assertThatThrownBy(() -> new Room(ROOM_ID, "Salle Atlas", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity");
    }

    @Test
    void should_reject_negative_capacity() {
        assertThatThrownBy(() -> new Room(ROOM_ID, "Salle Atlas", -3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacity");
    }
}