package com.doctorportal.appointment.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.LocalDate;

/**
 * Query object for hospital admin appointment search.
 *
 * All fields optional. If null -> not applied.
 */
public record HospitalAppointmentSearchRequest(
		LocalDate from,
		LocalDate to,
		AppointmentStatus status
) {
}

