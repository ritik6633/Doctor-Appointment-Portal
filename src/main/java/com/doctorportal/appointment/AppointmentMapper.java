package com.doctorportal.appointment;

import com.doctorportal.appointment.dto.AppointmentResponse;

public final class AppointmentMapper {
	private AppointmentMapper() {
	}

	public static AppointmentResponse toResponse(AppointmentEntity e) {
		return new AppointmentResponse(
			e.getId(),
			e.getDoctor().getId(),
			e.getDoctor().getUser().getName(),
			e.getPatient().getId(),
			e.getPatient().getName(),
			e.getHospital().getId(),
			e.getAppointmentDate(),
			e.getAppointmentTime(),
			e.getStatus(),
			e.getSymptoms()
		);
	}
}

