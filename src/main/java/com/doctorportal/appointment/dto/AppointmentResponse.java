package com.doctorportal.appointment.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
		Long id,
		Long doctorId,
		String doctorName,
		Long patientId,
		String patientName,
		Long hospitalId,
		LocalDate appointmentDate,
		LocalTime appointmentTime,
		AppointmentStatus status,
		String symptoms
) {
}

