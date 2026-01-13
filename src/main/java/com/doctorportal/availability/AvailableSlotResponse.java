package com.doctorportal.availability;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a single bookable appointment slot for a doctor.
 *
 * This is a derived (computed) DTO, not a persisted entity.
 */
public record AvailableSlotResponse(
		LocalDate date,
		LocalTime time
) {
}

