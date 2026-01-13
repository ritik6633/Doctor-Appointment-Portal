package com.doctorportal.doctor.dto;

import com.doctorportal.appointment.AppointmentStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Detailed doctor dashboard payload.
 *
 * Why separate DTO?
 * - Keeps the original DoctorDashboardResponse stable (if you already used it on frontend)
 * - Enables more "production-like" dashboard widgets (today list + weekly schedule)
 */
public record DoctorDashboardDetailResponse(
		long totalAppointments,
		long todaysAppointments,
		long completedAppointments,
		long cancelledAppointments,
		List<TodayAppointment> todaysSchedule,
		List<WeeklyAvailability> weeklyAvailability
) {
	public record TodayAppointment(
			Long appointmentId,
			Long patientId,
			String patientName,
			LocalDate date,
			LocalTime time,
			AppointmentStatus status,
			String symptoms
	) {
	}

	public record WeeklyAvailability(
			DayOfWeek dayOfWeek,
			LocalTime startTime,
			LocalTime endTime,
			int slotDurationMinutes
	) {
	}
}

