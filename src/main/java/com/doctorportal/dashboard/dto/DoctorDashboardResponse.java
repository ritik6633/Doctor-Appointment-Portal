package com.doctorportal.dashboard.dto;

public record DoctorDashboardResponse(
		long totalAppointments,
		long todaysAppointments,
		long completedAppointments,
		long cancelledAppointments
) {
}

