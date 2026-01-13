package com.doctorportal.dashboard.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HospitalAdminDashboardResponse(
		Long hospitalId,
		long totalDoctors,
		long totalAppointments,
		long bookedAppointments,
		long completedAppointments,
		long cancelledAppointments,
		List<RecentBooking> recentBookings,
		List<DepartmentStat> departmentStats
) {
	public record RecentBooking(
			Long appointmentId,
			Long doctorId,
			String doctorName,
			Long patientId,
			String patientName,
			LocalDate date,
			LocalTime time,
			AppointmentStatus status
	) {
	}

	public record DepartmentStat(
			Long departmentId,
			String departmentName,
			long totalDoctors
	) {
	}
}
