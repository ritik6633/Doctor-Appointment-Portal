package com.doctorportal.user;

import com.doctorportal.auth.RequireRole;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.hospital.HospitalEntity;
import com.doctorportal.hospital.HospitalRepository;
import com.doctorportal.user.dto.CreateHospitalAdminRequest;
import com.doctorportal.user.dto.CreateHospitalAdminResponse;
import com.doctorportal.user.dto.HospitalAdminUserResponse;
import com.doctorportal.user.dto.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Admin-only user management.
 */
@Service
@RequiredArgsConstructor
public class UserAdminService {
	private final UserRepository userRepository;
	private final HospitalRepository hospitalRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Transactional
	public CreateHospitalAdminResponse createHospitalAdmin(CreateHospitalAdminRequest req) {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		HospitalEntity hospital = hospitalRepository.findById(req.hospitalId())
				.orElseThrow(() -> new NotFoundException("Hospital not found: " + req.hospitalId()));

		userRepository.findByEmail(req.email().toLowerCase()).ifPresent(u -> {
			throw new BadRequestException("Email already exists");
		});

		UserEntity u = new UserEntity();
		u.setName(req.name());
		u.setEmail(req.email().toLowerCase());
		u.setPassword(passwordEncoder.encode(req.password()));
		u.setRole(Role.HOSPITAL_ADMIN);
		u.setPhone(req.phone());
		u.setGender(req.gender());
		u.setDateOfBirth(req.dateOfBirth());
		u.setHospital(hospital);
		u.setActive(true);

		Long userId = userRepository.save(u).getId();
		return new CreateHospitalAdminResponse(userId);
	}

	@Transactional(readOnly = true)
	public List<HospitalAdminUserResponse> listHospitalAdmins() {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		return userRepository.findByRole(Role.HOSPITAL_ADMIN).stream()
				.map(u -> new HospitalAdminUserResponse(
					u.getId(),
					u.getName(),
					u.getEmail(),
					u.getPhone(),
					u.getGender(),
					u.getDateOfBirth(),
					u.getHospital() != null ? u.getHospital().getId() : null,
					u.getHospital() != null ? u.getHospital().getName() : null,
					u.isActive()
				))
				.toList();
	}

	@Transactional
	public HospitalAdminUserResponse setHospitalAdminActive(Long userId, boolean active) {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		UserEntity u = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found: " + userId));
		if (u.getRole() != Role.HOSPITAL_ADMIN) {
			throw new BadRequestException("Only hospital admin users can be updated here");
		}
		u.setActive(active);

		return new HospitalAdminUserResponse(
			u.getId(),
			u.getName(),
			u.getEmail(),
			u.getPhone(),
			u.getGender(),
			u.getDateOfBirth(),
			u.getHospital() != null ? u.getHospital().getId() : null,
			u.getHospital() != null ? u.getHospital().getName() : null,
			u.isActive()
		);
	}

	@Transactional
	public void resetHospitalAdminPassword(Long userId, ResetPasswordRequest req) {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		UserEntity u = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found: " + userId));
		if (u.getRole() != Role.HOSPITAL_ADMIN) {
			throw new BadRequestException("Only hospital admin users can be updated here");
		}

		u.setPassword(passwordEncoder.encode(req.newPassword()));
	}
}
