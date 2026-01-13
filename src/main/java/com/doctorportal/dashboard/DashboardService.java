package com.doctorportal.dashboard;

import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.auth.RequireRole;
import com.doctorportal.appointment.AppointmentEntity;
import com.doctorportal.appointment.AppointmentRepository;
import com.doctorportal.appointment.AppointmentStatus;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.dashboard.dto.*;
import com.doctorportal.department.DepartmentRepository;
import com.doctorportal.doctor.DoctorEntity;
import com.doctorportal.doctor.DoctorRepository;
import com.doctorportal.hospital.HospitalEntity;
import com.doctorportal.hospital.HospitalRepository;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
	private final AppointmentRepository appointmentRepository;
	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;
	private final HospitalRepository hospitalRepository;
	private final DepartmentRepository departmentRepository;

	@Transactional(readOnly = true)
	public PatientDashboardResponse patientDashboard() {
		AuthPrincipal principal = RequireRole.requireAny(Role.PATIENT);
		var appts = appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(principal.userId());
		long total = appts.size();

		LocalDate today = LocalDate.now();
		long upcoming = appts.stream()
				.filter(a -> a.getStatus() == AppointmentStatus.BOOKED)
				.filter(a -> !a.getAppointmentDate().isBefore(today))
				.count();

		long past = appts.stream()
				.filter(a -> a.getAppointmentDate().isBefore(today) || a.getStatus() != AppointmentStatus.BOOKED)
				.count();

		var next = appts.stream()
				.filter(a -> a.getStatus() == AppointmentStatus.BOOKED)
				.filter(a -> !a.getAppointmentDate().isBefore(today))
				.min(Comparator.comparing(AppointmentEntity::getAppointmentDate)
						.thenComparing(AppointmentEntity::getAppointmentTime))
				.map(a -> new PatientDashboardResponse.NextAppointment(
						a.getId(),
						a.getDoctor().getId(),
						a.getDoctor().getUser().getName(),
						a.getAppointmentDate(),
						a.getAppointmentTime(),
						a.getStatus()
				))
				.orElse(null);

		return new PatientDashboardResponse(total, upcoming, past, next);
	}

	@Transactional(readOnly = true)
	public DoctorDashboardResponse doctorDashboard() {
		AuthPrincipal principal = RequireRole.requireAny(Role.DOCTOR);
		DoctorEntity doctor = doctorRepository.findByUserId(principal.userId())
				.orElseThrow(() -> new NotFoundException("Doctor profile not found for user: " + principal.userId()));
		var appts = appointmentRepository.findByDoctorIdOrderByAppointmentDateDescAppointmentTimeDesc(doctor.getId());

		long total = appts.size();
		LocalDate today = LocalDate.now();
		long todayCount = appts.stream().filter(a -> a.getAppointmentDate().equals(today)).count();
		long completed = appts.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
		long cancelled = appts.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();

		return new DoctorDashboardResponse(total, todayCount, completed, cancelled);
	}

	@Transactional(readOnly = true)
	public HospitalAdminDashboardResponse hospitalAdminDashboard() {
		AuthPrincipal principal = RequireRole.requireAny(Role.HOSPITAL_ADMIN);
		UserEntity admin = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));
		if (admin.getHospital() == null) {
			throw new NotFoundException("Hospital not assigned to admin");
		}
		if (!admin.getHospital().isActive()) {
			throw new BadRequestException("Hospital is inactive");
		}
		if (!admin.getHospital().isApproved()) {
			throw new BadRequestException("Hospital is not approved");
		}

		Long hospitalId = admin.getHospital().getId();

		long doctors = doctorRepository.findByHospitalId(hospitalId).stream().filter(DoctorEntity::isActive).count();
		var appts = appointmentRepository.findByHospitalIdOrderByAppointmentDateDescAppointmentTimeDesc(hospitalId);

		long total = appts.size();
		long booked = appts.stream().filter(a -> a.getStatus() == AppointmentStatus.BOOKED).count();
		long completed = appts.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
		long cancelled = appts.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();

		List<HospitalAdminDashboardResponse.RecentBooking> recent = appts.stream()
				.limit(10)
				.map(a -> new HospitalAdminDashboardResponse.RecentBooking(
						a.getId(),
						a.getDoctor().getId(),
						a.getDoctor().getUser().getName(),
						a.getPatient().getId(),
						a.getPatient().getName(),
						a.getAppointmentDate(),
						a.getAppointmentTime(),
						a.getStatus()
				))
				.toList();

		var departmentStats = departmentRepository.findByHospitalIdAndActiveTrue(hospitalId).stream()
				.map(d -> new HospitalAdminDashboardResponse.DepartmentStat(
						d.getId(),
						d.getName(),
						doctorRepository.findByHospitalIdAndDepartmentId(hospitalId, d.getId())
								.stream().filter(DoctorEntity::isActive).count()
				))
				.toList();

		return new HospitalAdminDashboardResponse(hospitalId, doctors, total, booked, completed, cancelled, recent, departmentStats);
	}

	@Transactional(readOnly = true)
	public DeveloperAdminDashboardResponse developerAdminDashboard() {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		long totalHospitals = hospitalRepository.count();
		long approved = hospitalRepository.countByApprovedTrue();
		long pending = totalHospitals - approved;

		long users = userRepository.count();
		long totalPatients = userRepository.countByRole(Role.PATIENT);
		long totalDoctors = userRepository.countByRole(Role.DOCTOR);
		long totalHospitalAdmins = userRepository.countByRole(Role.HOSPITAL_ADMIN);

		List<DeveloperAdminDashboardResponse.RecentHospital> recent = hospitalRepository.findAll().stream()
				.sorted(Comparator.comparing(HospitalEntity::getCreatedAt).reversed())
				.limit(10)
				.map(h -> new DeveloperAdminDashboardResponse.RecentHospital(
						h.getId(),
						h.getName(),
						h.getCity(),
						h.isApproved(),
						h.isActive()
				))
				.toList();

		return new DeveloperAdminDashboardResponse(
				totalHospitals,
				approved,
				pending,
				users,
				totalPatients,
				totalDoctors,
				totalHospitalAdmins,
				recent
		);
	}
}

