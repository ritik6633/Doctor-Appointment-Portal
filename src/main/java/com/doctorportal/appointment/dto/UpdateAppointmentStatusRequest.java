package com.doctorportal.appointment.dto;

import com.doctorportal.appointment.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequest(
		@NotNull AppointmentStatus status
) {
}

