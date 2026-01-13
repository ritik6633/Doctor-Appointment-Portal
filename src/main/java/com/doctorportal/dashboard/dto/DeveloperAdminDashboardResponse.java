package com.doctorportal.dashboard.dto;

import java.util.List;

public record DeveloperAdminDashboardResponse(
		long totalHospitals,
		long approvedHospitals,
		long pendingHospitals,
		long totalUsers,
		long totalPatients,
		long totalDoctors,
		long totalHospitalAdmins,
		List<RecentHospital> recentHospitals
) {
	public record RecentHospital(
			Long hospitalId,
			String name,
			String city,
			boolean approved,
			boolean active
	) {
	}
}
