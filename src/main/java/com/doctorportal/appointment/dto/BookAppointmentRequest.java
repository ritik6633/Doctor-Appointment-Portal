package com.doctorportal.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookAppointmentRequest(
		@NotNull Long doctorId,
		@NotNull LocalDate appointmentDate,
		@NotNull LocalTime appointmentTime,
		@NotBlank String symptoms
) {
}

