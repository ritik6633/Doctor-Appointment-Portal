package com.doctorportal.appointment;

import com.doctorportal.appointment.dto.AppointmentResponse;
import com.doctorportal.appointment.dto.DeveloperHospitalAppointmentResponse;
import com.doctorportal.appointment.dto.HospitalAppointmentResponse;

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
			e.getSymptoms(),
			e.getCreatedAt()
		);
	}

	public static HospitalAppointmentResponse toHospitalResponse(AppointmentEntity e) {
		return new HospitalAppointmentResponse(
			e.getId(),
			e.getDoctor().getId(),
			e.getDoctor().getUser().getName(),
			e.getPatient().getId(),
			e.getPatient().getName(),
			e.getAppointmentDate(),
			e.getAppointmentTime(),
			e.getStatus(),
			e.getSymptoms()
		);
	}

	public static DeveloperHospitalAppointmentResponse toDeveloperAuditResponse(AppointmentEntity e) {
		return new DeveloperHospitalAppointmentResponse(
			e.getId(),
			e.getHospital().getId(),
			e.getHospital().getName(),
			e.getDoctor().getId(),
			e.getDoctor().getUser().getName(),
			e.getPatient().getId(),
			e.getPatient().getName(),
			e.getAppointmentDate(),
			e.getAppointmentTime(),
			e.getStatus(),
			e.getSymptoms(),
			e.getCreatedAt()
		);
	}
}
