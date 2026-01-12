package com.doctorportal.dashboard.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record PatientDashboardResponse(
		long totalAppointments,
		long upcomingAppointments,
		long pastAppointments,
		NextAppointment nextAppointment
) {
	public record NextAppointment(
			Long appointmentId,
			Long doctorId,
			String doctorName,
			LocalDate date,
			LocalTime time,
			AppointmentStatus status
	) {
	}
}

