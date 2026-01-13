package com.doctorportal.appointment.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment response used for hospital admin views.
 * Includes patient identity (allowed for hospital staff), doctor name, etc.
 */
public record HospitalAppointmentResponse(
		Long id,
		Long doctorId,
		String doctorName,
		Long patientId,
		String patientName,
		LocalDate appointmentDate,
		LocalTime appointmentTime,
		AppointmentStatus status,
		String symptoms
) {
}

