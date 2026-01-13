package com.doctorportal.appointment;

import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.auth.RequireRole;
import com.doctorportal.appointment.dto.AppointmentResponse;
import com.doctorportal.appointment.dto.BookAppointmentRequest;
import com.doctorportal.appointment.dto.HospitalAppointmentResponse;
import com.doctorportal.appointment.dto.HospitalAppointmentSearchRequest;
import com.doctorportal.appointment.dto.UpdateAppointmentStatusRequest;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.ForbiddenException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.doctor.DoctorAvailabilityEntity;
import com.doctorportal.doctor.DoctorAvailabilityRepository;
import com.doctorportal.doctor.DoctorEntity;
import com.doctorportal.doctor.DoctorRepository;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
	private final AppointmentRepository appointmentRepository;
	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;
	private final DoctorAvailabilityRepository availabilityRepository;

	@Transactional
	public AppointmentResponse book(BookAppointmentRequest req) {
		AuthPrincipal principal = RequireRole.requireAny(Role.PATIENT);

		UserEntity patient = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));
		if (!patient.isActive()) {
			throw new BadRequestException("Patient is inactive");
		}

		DoctorEntity doctor = doctorRepository.findById(req.doctorId())
				.orElseThrow(() -> new NotFoundException("Doctor not found: " + req.doctorId()));
		if (!doctor.isActive() || !doctor.getHospital().isActive() || !doctor.getHospital().isApproved()) {
			throw new BadRequestException("Doctor is not available for booking");
		}

		LocalDate date = req.appointmentDate();
		LocalTime time = req.appointmentTime();
		if (date.isBefore(LocalDate.now())) {
			throw new BadRequestException("Appointment date cannot be in the past");
		}
		if (date.isEqual(LocalDate.now()) && !time.isAfter(LocalTime.now())) {
			throw new BadRequestException("Appointment time must be in the future");
		}

		assertIsWithinDoctorAvailabilityGrid(doctor.getId(), date, time);

		boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
				doctor.getId(),
				date,
				time,
				List.of(AppointmentStatus.BOOKED)
		);
		if (conflict) {
			throw new BadRequestException("Time slot already booked");
		}

		AppointmentEntity appt = new AppointmentEntity();
		appt.setDoctor(doctor);
		appt.setPatient(patient);
		appt.setHospital(doctor.getHospital());
		appt.setAppointmentDate(date);
		appt.setAppointmentTime(time);
		appt.setStatus(AppointmentStatus.BOOKED);
		appt.setSymptoms(req.symptoms());

		return AppointmentMapper.toResponse(appointmentRepository.save(appt));
	}

	private void assertIsWithinDoctorAvailabilityGrid(Long doctorId, LocalDate date, LocalTime time) {
		var rulesForDay = availabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek());
		if (rulesForDay.isEmpty()) {
			throw new BadRequestException("Doctor is not available on " + date.getDayOfWeek());
		}

		DoctorAvailabilityEntity rule = rulesForDay.get(0);
		if (!time.isBefore(rule.getEndTime()) || time.isBefore(rule.getStartTime())) {
			throw new BadRequestException("Requested time is outside doctor's availability window");
		}

		int step = rule.getSlotDurationMinutes();
		long minutesFromStart = java.time.Duration.between(rule.getStartTime(), time).toMinutes();
		if (minutesFromStart % step != 0) {
			throw new BadRequestException("Requested time is not aligned to slot duration of " + step + " minutes");
		}

		// Ensure it's a valid start slot (must fit entirely in window)
		if (time.plusMinutes(step).isAfter(rule.getEndTime())) {
			throw new BadRequestException("Requested slot exceeds doctor's availability window");
		}
	}

	@Transactional(readOnly = true)
	public List<AppointmentResponse> listForPatient(Long patientId) {
		AuthPrincipal principal = RequireRole.requireAny(Role.PATIENT);
		if (!principal.userId().equals(patientId)) {
			throw new ForbiddenException("Patient can only view their own appointments");
		}
		return appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patientId)
				.stream().map(AppointmentMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<AppointmentResponse> listForDoctor(Long doctorUserId) {
		AuthPrincipal principal = RequireRole.requireAny(Role.DOCTOR);
		if (!principal.userId().equals(doctorUserId)) {
			throw new ForbiddenException("Doctor can only view their own appointments");
		}

		DoctorEntity doctor = doctorRepository.findByUserId(doctorUserId)
				.orElseThrow(() -> new NotFoundException("Doctor profile not found for user: " + doctorUserId));

		return appointmentRepository.findByDoctorIdOrderByAppointmentDateDescAppointmentTimeDesc(doctor.getId())
				.stream().map(AppointmentMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<HospitalAppointmentResponse> listForHospital(Long hospitalId) {
		return searchForHospital(hospitalId, new HospitalAppointmentSearchRequest(null, null, null));
	}

	@Transactional(readOnly = true)
	public List<HospitalAppointmentResponse> searchForHospital(Long hospitalId, HospitalAppointmentSearchRequest search) {
		AuthPrincipal principal = RequireRole.requireAny(Role.HOSPITAL_ADMIN);

		UserEntity admin = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));
		if (admin.getHospital() == null) {
			throw new ForbiddenException("Hospital admin is not assigned to a hospital");
		}
		if (!admin.getHospital().getId().equals(hospitalId)) {
			throw new ForbiddenException("Hospital admin can only view their own hospital appointments");
		}

		LocalDate from = search.from();
		LocalDate to = search.to();
		if (from != null && to != null && to.isBefore(from)) {
			throw new BadRequestException("to must be on or after from");
		}

		return appointmentRepository.findByHospitalIdOrderByAppointmentDateDescAppointmentTimeDesc(hospitalId)
				.stream()
				.filter(a -> from == null || !a.getAppointmentDate().isBefore(from))
				.filter(a -> to == null || !a.getAppointmentDate().isAfter(to))
				.filter(a -> search.status() == null || a.getStatus() == search.status())
				.map(AppointmentMapper::toHospitalResponse)
				.toList();
	}

	@Transactional
	public AppointmentResponse cancel(Long appointmentId) {
		AuthPrincipal principal = RequireRole.requireAny(Role.PATIENT);
		AppointmentEntity appt = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new NotFoundException("Appointment not found: " + appointmentId));

		if (!appt.getPatient().getId().equals(principal.userId())) {
			throw new ForbiddenException("Patient can only cancel their own appointment");
		}
		if (appt.getStatus() != AppointmentStatus.BOOKED) {
			throw new BadRequestException("Only BOOKED appointments can be cancelled");
		}
		if (appt.getAppointmentDate().isBefore(LocalDate.now())) {
			throw new BadRequestException("Past appointments cannot be cancelled");
		}

		appt.setStatus(AppointmentStatus.CANCELLED);
		return AppointmentMapper.toResponse(appt);
	}

	@Transactional
	public AppointmentResponse updateStatus(Long appointmentId, UpdateAppointmentStatusRequest req) {
		AuthPrincipal principal = RequireRole.requireAny(Role.DOCTOR);
		AppointmentEntity appt = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new NotFoundException("Appointment not found: " + appointmentId));

		DoctorEntity doctor = doctorRepository.findByUserId(principal.userId())
				.orElseThrow(() -> new NotFoundException("Doctor profile not found for user: " + principal.userId()));
		if (!appt.getDoctor().getId().equals(doctor.getId())) {
			throw new ForbiddenException("Doctor can only update appointments assigned to them");
		}

		if (!doctor.isActive()) {
			throw new BadRequestException("Doctor is inactive");
		}
		if (!doctor.getHospital().isActive()) {
			throw new BadRequestException("Hospital is inactive");
		}
		if (!doctor.getHospital().isApproved()) {
			throw new BadRequestException("Hospital is not approved");
		}

		AppointmentStatus target = req.status();
		if (target == AppointmentStatus.CANCELLED) {
			throw new BadRequestException("Doctor cannot cancel appointments");
		}
		if (target == AppointmentStatus.COMPLETED && appt.getStatus() != AppointmentStatus.BOOKED) {
			throw new BadRequestException("Only BOOKED appointments can be completed");
		}

		appt.setStatus(target);
		return AppointmentMapper.toResponse(appt);
	}
}

