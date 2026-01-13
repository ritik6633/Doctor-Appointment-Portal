package com.doctorportal.availability;

import com.doctorportal.appointment.AppointmentRepository;
import com.doctorportal.appointment.AppointmentStatus;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.doctor.DoctorAvailabilityEntity;
import com.doctorportal.doctor.DoctorAvailabilityRepository;
import com.doctorportal.doctor.DoctorEntity;
import com.doctorportal.doctor.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Read-only availability module.
 *
 * Business rules:
 * - Only returns future slots (including today if time is in the future)
 * - Slots are based on doctor's weekly availability rules
 * - Already BOOKED slots are excluded
 */
@Service
@RequiredArgsConstructor
public class AvailabilityService {
	private final DoctorRepository doctorRepository;
	private final DoctorAvailabilityRepository doctorAvailabilityRepository;
	private final AppointmentRepository appointmentRepository;

	@Transactional(readOnly = true)
	public List<AvailableSlotResponse> listAvailableSlots(Long doctorId, LocalDate from, int days) {
		if (days <= 0 || days > 30) {
			throw new BadRequestException("days must be between 1 and 30");
		}
		LocalDate start = (from == null) ? LocalDate.now() : from;
		if (start.isBefore(LocalDate.now())) {
			throw new BadRequestException("from cannot be in the past");
		}

		DoctorEntity doctor = doctorRepository.findById(doctorId)
				.orElseThrow(() -> new NotFoundException("Doctor not found: " + doctorId));
		if (!doctor.isActive() || !doctor.getHospital().isActive() || !doctor.getHospital().isApproved()) {
			throw new BadRequestException("Doctor is not available for booking");
		}

		List<DoctorAvailabilityEntity> weeklyRules = doctorAvailabilityRepository.findByDoctorId(doctorId);
		if (weeklyRules.isEmpty()) {
			return List.of();
		}

		List<AvailableSlotResponse> result = new ArrayList<>();
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();

		for (int i = 0; i < days; i++) {
			LocalDate date = start.plusDays(i);
			DayOfWeek dow = date.getDayOfWeek();

			for (DoctorAvailabilityEntity rule : weeklyRules) {
				if (rule.getDayOfWeek() != dow) continue;

				int step = rule.getSlotDurationMinutes();
				LocalTime t = rule.getStartTime();

				while (!t.plusMinutes(step).isAfter(rule.getEndTime())) {
					// Only future slots
					if (date.isAfter(today) || (date.isEqual(today) && t.isAfter(now))) {
						boolean booked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
								doctorId,
								date,
								t,
								List.of(AppointmentStatus.BOOKED)
						);
						if (!booked) {
							result.add(new AvailableSlotResponse(date, t));
						}
					}
					t = t.plusMinutes(step);
				}
			}
		}

		return result;
	}
}

