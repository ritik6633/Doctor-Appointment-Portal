package com.doctorportal.doctor;

import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.auth.RequireRole;
import com.doctorportal.appointment.AppointmentRepository;
import com.doctorportal.appointment.AppointmentStatus;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.department.DepartmentEntity;
import com.doctorportal.department.DepartmentRepository;
import com.doctorportal.doctor.dto.CreateDoctorRequest;
import com.doctorportal.doctor.dto.DoctorDashboardDetailResponse;
import com.doctorportal.doctor.dto.DoctorResponse;
import com.doctorportal.hospital.HospitalEntity;
import com.doctorportal.hospital.HospitalRepository;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;
	private final HospitalRepository hospitalRepository;
	private final DepartmentRepository departmentRepository;
	private final DoctorAvailabilityRepository doctorAvailabilityRepository;
	private final AppointmentRepository appointmentRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Transactional(readOnly = true)
	public List<DoctorResponse> listByHospital(Long hospitalId) {
		return doctorRepository.findByHospitalId(hospitalId).stream()
				.filter(d -> d.isActive() && d.getHospital().isActive() && d.getHospital().isApproved())
				.map(DoctorMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<DoctorResponse> listByHospitalAndDepartment(Long hospitalId, Long departmentId) {
		return doctorRepository.findByHospitalIdAndDepartmentId(hospitalId, departmentId).stream()
				.filter(d -> d.isActive() && d.getHospital().isActive() && d.getHospital().isApproved())
				.map(DoctorMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public DoctorDashboardDetailResponse doctorDashboardDetail() {
		AuthPrincipal principal = RequireRole.requireAny(Role.DOCTOR);
		DoctorEntity doctor = doctorRepository.findByUserId(principal.userId())
				.orElseThrow(() -> new NotFoundException("Doctor profile not found for user: " + principal.userId()));

		if (!doctor.isActive()) {
			throw new BadRequestException("Doctor is inactive");
		}
		if (!doctor.getHospital().isActive()) {
			throw new BadRequestException("Hospital is inactive");
		}
		if (!doctor.getHospital().isApproved()) {
			throw new BadRequestException("Hospital is not approved");
		}

		var all = appointmentRepository.findByDoctorIdOrderByAppointmentDateDescAppointmentTimeDesc(doctor.getId());
		long total = all.size();
		long completed = all.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
		long cancelled = all.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();

		LocalDate today = LocalDate.now();
		var todayAppts = appointmentRepository.findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctor.getId(), today);
		long todayCount = todayAppts.size();

		var todaySchedule = todayAppts.stream()
				.map(a -> new DoctorDashboardDetailResponse.TodayAppointment(
						a.getId(),
						a.getPatient().getId(),
						a.getPatient().getName(),
						a.getAppointmentDate(),
						a.getAppointmentTime(),
						a.getStatus(),
						a.getSymptoms()
				))
				.toList();

		var weekly = doctorAvailabilityRepository.findByDoctorIdOrderByDayOfWeekAscStartTimeAsc(doctor.getId()).stream()
				.map(a -> new DoctorDashboardDetailResponse.WeeklyAvailability(
						a.getDayOfWeek(),
						a.getStartTime(),
						a.getEndTime(),
						a.getSlotDurationMinutes()
				))
				.toList();

		return new DoctorDashboardDetailResponse(total, todayCount, completed, cancelled, todaySchedule, weekly);
	}

	@Transactional
	public DoctorResponse create(CreateDoctorRequest req) {
		var principal = RequireRole.requireAny(Role.HOSPITAL_ADMIN);

		UserEntity admin = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));
		if (admin.getHospital() == null || !admin.getHospital().getId().equals(req.hospitalId())) {
			throw new BadRequestException("Hospital admin can only add doctors for their own hospital");
		}

		HospitalEntity hospital = hospitalRepository.findById(req.hospitalId())
				.orElseThrow(() -> new NotFoundException("Hospital not found: " + req.hospitalId()));
		if (!hospital.isApproved() || !hospital.isActive()) {
			throw new BadRequestException("Hospital must be approved and active");
		}

		DepartmentEntity dept = departmentRepository.findById(req.departmentId())
				.orElseThrow(() -> new NotFoundException("Department not found: " + req.departmentId()));
		if (!dept.getHospital().getId().equals(hospital.getId())) {
			throw new BadRequestException("Department does not belong to the hospital");
		}

		userRepository.findByEmail(req.email().toLowerCase()).ifPresent(u -> {
			throw new BadRequestException("Email already exists");
		});

		UserEntity user = new UserEntity();
		user.setName(req.name());
		user.setEmail(req.email().toLowerCase());
		user.setPassword(passwordEncoder.encode(req.password()));
		user.setRole(Role.DOCTOR);
		user.setPhone(req.phone());
		user.setGender(req.gender());
		user.setDateOfBirth(req.dateOfBirth());
		user.setHospital(hospital);
		user.setActive(true);
		user = userRepository.save(user);

		DoctorEntity doctor = new DoctorEntity();
		doctor.setUser(user);
		doctor.setHospital(hospital);
		doctor.setDepartment(dept);
		doctor.setSpecialization(req.specialization());
		doctor.setQualification(req.qualification());
		doctor.setExperienceYears(req.experienceYears());
		doctor.setConsultationFee(req.consultationFee());
		doctor.setActive(true);

		return DoctorMapper.toResponse(doctorRepository.save(doctor));
	}

	@Transactional(readOnly = true)
	public DoctorEntity getById(Long doctorId) {
		return doctorRepository.findById(doctorId)
				.orElseThrow(() -> new NotFoundException("Doctor not found: " + doctorId));
	}

	@Transactional(readOnly = true)
	public DoctorEntity getByUserId(Long userId) {
		return doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("Doctor profile not found for user: " + userId));
	}
}

