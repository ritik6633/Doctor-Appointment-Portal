package com.doctorportal.doctor;

import com.doctorportal.doctor.dto.DoctorAvailabilityResponse;

public final class DoctorAvailabilityMapper {
	private DoctorAvailabilityMapper() {
	}

	public static DoctorAvailabilityResponse toResponse(DoctorAvailabilityEntity e) {
		return new DoctorAvailabilityResponse(
			e.getId(),
			e.getDoctor().getId(),
			e.getDayOfWeek(),
			e.getStartTime(),
			e.getEndTime(),
			e.getSlotDurationMinutes()
		);
	}
}

