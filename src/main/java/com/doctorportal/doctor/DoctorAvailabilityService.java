package com.doctorportal.doctor;

import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.auth.RequireRole;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.ForbiddenException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.doctor.dto.DoctorAvailabilityResponse;
import com.doctorportal.doctor.dto.UpsertAvailabilityRequest;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {
	private final DoctorAvailabilityRepository availabilityRepository;
	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<DoctorAvailabilityResponse> listByDoctor(Long doctorId) {
		return availabilityRepository.findByDoctorId(doctorId).stream()
				.map(DoctorAvailabilityMapper::toResponse)
				.toList();
	}

	@Transactional
	public DoctorAvailabilityResponse upsert(Long doctorId, UpsertAvailabilityRequest req) {
		AuthPrincipal principal = RequireRole.requireAny(Role.DOCTOR, Role.HOSPITAL_ADMIN);
		DoctorEntity doctor = doctorRepository.findById(doctorId)
				.orElseThrow(() -> new NotFoundException("Doctor not found: " + doctorId));
		assertCanManage(principal, doctor);

		if (!doctor.isActive()) {
			throw new BadRequestException("Doctor is inactive");
		}
		if (!doctor.getHospital().isActive()) {
			throw new BadRequestException("Hospital is inactive");
		}
		if (!doctor.getHospital().isApproved()) {
			throw new BadRequestException("Hospital is not approved");
		}

		if (req.slotDurationMinutes() == null || req.slotDurationMinutes() <= 0) {
			throw new BadRequestException("slotDurationMinutes must be > 0");
		}
		if (!req.endTime().isAfter(req.startTime())) {
			throw new BadRequestException("endTime must be after startTime");
		}

		long windowMinutes = Duration.between(req.startTime(), req.endTime()).toMinutes();
		if (windowMinutes < req.slotDurationMinutes()) {
			throw new BadRequestException("Availability window is smaller than slot duration");
		}
		if (windowMinutes % req.slotDurationMinutes() != 0) {
			throw new BadRequestException("Availability window must be divisible by slotDurationMinutes");
		}

		// Upsert with overlap checks:
		// - allow exactly one rule per day for now
		// - block additional rules to avoid ambiguous slot generation
		List<DoctorAvailabilityEntity> existing = availabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, req.dayOfWeek());
		DoctorAvailabilityEntity e = existing.isEmpty() ? new DoctorAvailabilityEntity() : existing.get(0);

		// If there are multiple rows (shouldn't happen), don't silently pick one.
		if (existing.size() > 1) {
			throw new BadRequestException("Multiple availability rules exist for this day. Please clean up data.");
		}

		e.setDoctor(doctor);
		e.setDayOfWeek(req.dayOfWeek());
		e.setStartTime(req.startTime());
		e.setEndTime(req.endTime());
		e.setSlotDurationMinutes(req.slotDurationMinutes());

		return DoctorAvailabilityMapper.toResponse(availabilityRepository.save(e));
	}

	private void assertCanManage(AuthPrincipal principal, DoctorEntity doctor) {
		if (principal.role() == Role.DOCTOR) {
			DoctorEntity self = doctorRepository.findByUserId(principal.userId())
					.orElseThrow(() -> new NotFoundException("Doctor profile not found for user: " + principal.userId()));
			if (!self.getId().equals(doctor.getId())) {
				throw new ForbiddenException("Doctor can only manage their own availability");
			}
			return;
		}

		// Hospital admin
		UserEntity admin = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));
		if (admin.getHospital() == null || !admin.getHospital().getId().equals(doctor.getHospital().getId())) {
			throw new ForbiddenException("Hospital admin can only manage doctors of their hospital");
		}
	}
}

