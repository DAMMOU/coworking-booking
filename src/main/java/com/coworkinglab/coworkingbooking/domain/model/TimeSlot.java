package com.coworkinglab.coworkingbooking.domain.model;

import com.coworkinglab.coworkingbooking.domain.exception.InvalidTimeSlotException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Créneau horaire immuable.
 * Règles :
 *  - début strictement antérieur à la fin
 *  - durée minimale 30 minutes
 *  - durée maximale 8 heures
 */
public record TimeSlot(LocalDateTime start, LocalDateTime end) {

    public static final Duration MIN_DURATION = Duration.ofMinutes(30);
    public static final Duration MAX_DURATION = Duration.ofHours(8);

    public TimeSlot {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");

        if (!start.isBefore(end)) {
            throw new InvalidTimeSlotException("start must be strictly before end");
        }

        Duration duration = Duration.between(start, end);
        if (duration.compareTo(MIN_DURATION) < 0) {
            throw new InvalidTimeSlotException(
                    "duration must be at least " + MIN_DURATION.toMinutes() + " minutes");
        }
        if (duration.compareTo(MAX_DURATION) > 0) {
            throw new InvalidTimeSlotException(
                    "duration must be at most " + MAX_DURATION.toHours() + " hours");
        }
    }

    public Duration duration() {
        return Duration.between(start, end);
    }

    /**
     * Deux créneaux se chevauchent si l'un commence avant que l'autre ne finisse,
     * et inversement. Deux créneaux adjacents (fin de l'un = début de l'autre) ne se chevauchent pas.
     */
    public boolean overlapsWith(TimeSlot other) {
        Objects.requireNonNull(other, "other must not be null");
        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }

    public boolean isInThePast(LocalDateTime now) {
        return end.isBefore(now) || end.isEqual(now);
    }
}