package com.doctorportal.doctor.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record UpsertAvailabilityRequest(
		@NotNull DayOfWeek dayOfWeek,
		@NotNull LocalTime startTime,
		@NotNull LocalTime endTime,
		@NotNull Integer slotDurationMinutes
) {
}

