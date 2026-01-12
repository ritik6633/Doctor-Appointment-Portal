 package com.doctorportal.doctor.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record DoctorAvailabilityResponse(
		Long id,
		Long doctorId,
		DayOfWeek dayOfWeek,
		LocalTime startTime,
		LocalTime endTime,
		Integer slotDurationMinutes
) {
}

