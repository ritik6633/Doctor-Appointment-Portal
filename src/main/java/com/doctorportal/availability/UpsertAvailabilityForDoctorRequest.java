package com.doctorportal.availability;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * API-level request for POST /availability.
 *
 * We keep it separate from the doctor module DTO so the route structure matches requirements.
 */
public record UpsertAvailabilityForDoctorRequest(
		@NotNull Long doctorId,
		@NotNull DayOfWeek dayOfWeek,
		@NotNull LocalTime startTime,
		@NotNull LocalTime endTime,
		@NotNull Integer slotDurationMinutes
) {
}

