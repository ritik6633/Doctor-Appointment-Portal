package com.doctorportal.dashboard.dto;

public record DeveloperAdminDashboardResponse(
		long totalHospitals,
		long approvedHospitals,
		long pendingHospitals,
		long totalUsers
) {
}

