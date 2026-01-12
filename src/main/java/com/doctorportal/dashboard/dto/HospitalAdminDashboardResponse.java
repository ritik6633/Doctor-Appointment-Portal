package com.doctorportal.dashboard.dto;

public record HospitalAdminDashboardResponse(
		Long hospitalId,
		long totalDoctors,
		long totalAppointments,
		long bookedAppointments
) {
}

