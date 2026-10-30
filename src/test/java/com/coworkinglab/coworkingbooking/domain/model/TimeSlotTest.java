package com.coworkinglab.coworkingbooking.domain.model;

import com.coworkinglab.coworkingbooking.domain.exception.InvalidTimeSlotException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeSlotTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 15, 14, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 6, 15, 16, 0);

    // --- Validation ---

    @Test
    void should_create_valid_time_slot() {
        TimeSlot slot = new TimeSlot(START, END);

        assertThat(slot.start()).isEqualTo(START);
        assertThat(slot.end()).isEqualTo(END);
    }

    @Test
    void should_reject_null_start() {
        assertThatThrownBy(() -> new TimeSlot(null, END))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_null_end() {
        assertThatThrownBy(() -> new TimeSlot(START, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_start_equal_to_end() {
        assertThatThrownBy(() -> new TimeSlot(START, START))
                .isInstanceOf(InvalidTimeSlotException.class)
                .hasMessageContaining("strictly before");
    }

    @Test
    void should_reject_start_after_end() {
        assertThatThrownBy(() -> new TimeSlot(END, START))
                .isInstanceOf(InvalidTimeSlotException.class)
                .hasMessageContaining("strictly before");
    }

    @Test
    void should_reject_duration_shorter_than_minimum() {
        LocalDateTime end = START.plusMinutes(15);
        assertThatThrownBy(() -> new TimeSlot(START, end))
                .isInstanceOf(InvalidTimeSlotException.class)
                .hasMessageContaining("at least 30 minutes");
    }

    @Test
    void should_accept_exactly_minimum_duration() {
        TimeSlot slot = new TimeSlot(START, START.plusMinutes(30));
        assertThat(slot.duration()).hasMinutes(30);
    }

    @Test
    void should_reject_duration_longer_than_maximum() {
        LocalDateTime end = START.plusHours(9);
        assertThatThrownBy(() -> new TimeSlot(START, end))
                .isInstanceOf(InvalidTimeSlotException.class)
                .hasMessageContaining("at most 8 hours");
    }

    @Test
    void should_accept_exactly_maximum_duration() {
        TimeSlot slot = new TimeSlot(START, START.plusHours(8));
        assertThat(slot.duration()).hasHours(8);
    }

    // --- Chevauchement ---

    @Test
    void should_overlap_when_slots_partially_overlap() {
        TimeSlot a = new TimeSlot(START, END);                    // 14h - 16h
        TimeSlot b = new TimeSlot(START.plusHours(1), END.plusHours(1)); // 15h - 17h

        assertThat(a.overlapsWith(b)).isTrue();
        assertThat(b.overlapsWith(a)).isTrue();
    }

    @Test
    void should_overlap_when_one_slot_contains_the_other() {
        TimeSlot big = new TimeSlot(START, END);                  // 14h - 16h
        TimeSlot small = new TimeSlot(START.plusMinutes(30), START.plusMinutes(90)); // 14h30 - 15h30

        assertThat(big.overlapsWith(small)).isTrue();
        assertThat(small.overlapsWith(big)).isTrue();
    }

    @Test
    void should_overlap_when_slots_are_identical() {
        TimeSlot a = new TimeSlot(START, END);
        TimeSlot b = new TimeSlot(START, END);

        assertThat(a.overlapsWith(b)).isTrue();
    }

    @Test
    void should_not_overlap_when_slots_are_adjacent() {
        TimeSlot a = new TimeSlot(START, END);                    // 14h - 16h
        TimeSlot b = new TimeSlot(END, END.plusHours(2));         // 16h - 18h

        assertThat(a.overlapsWith(b)).isFalse();
        assertThat(b.overlapsWith(a)).isFalse();
    }

    @Test
    void should_not_overlap_when_slots_are_disjoint() {
        TimeSlot a = new TimeSlot(START, END);                    // 14h - 16h
        TimeSlot b = new TimeSlot(END.plusHours(1), END.plusHours(3)); // 17h - 19h

        assertThat(a.overlapsWith(b)).isFalse();
    }

    @Test
    void should_not_overlap_when_other_slot_is_before() {
        TimeSlot a = new TimeSlot(START, END);                    // 14h - 16h
        TimeSlot b = new TimeSlot(START.minusHours(2), START.minusHours(1)); // 12h - 13h

        assertThat(a.overlapsWith(b)).isFalse();
    }

    @Test
    void should_throw_when_checking_overlap_with_null() {
        TimeSlot a = new TimeSlot(START, END);

        assertThatThrownBy(() -> a.overlapsWith(null))
                .isInstanceOf(NullPointerException.class);
    }

    // --- isInThePast ---

    @Test
    void should_be_in_the_past_when_end_is_before_now() {
        TimeSlot slot = new TimeSlot(START, END);
        LocalDateTime now = END.plusHours(1);

        assertThat(slot.isInThePast(now)).isTrue();
    }

    @Test
    void should_be_in_the_past_when_end_equals_now() {
        TimeSlot slot = new TimeSlot(START, END);

        assertThat(slot.isInThePast(END)).isTrue();
    }

    @Test
    void should_not_be_in_the_past_when_end_is_after_now() {
        TimeSlot slot = new TimeSlot(START, END);
        LocalDateTime now = START;

        assertThat(slot.isInThePast(now)).isFalse();
    }
}
