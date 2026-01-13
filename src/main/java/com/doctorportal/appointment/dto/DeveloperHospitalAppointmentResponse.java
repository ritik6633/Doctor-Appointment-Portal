package com.doctorportal.appointment.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Developer-admin appointment audit view.
 *
 * Includes hospital name so platform admin can audit cross-tenant activity.
 */
public record DeveloperHospitalAppointmentResponse(
		Long id,
		Long hospitalId,
		String hospitalName,
		Long doctorId,
		String doctorName,
		Long patientId,
		String patientName,
		LocalDate appointmentDate,
		LocalTime appointmentTime,
		AppointmentStatus status,
		String symptoms,
		Instant createdAt
) {
}

