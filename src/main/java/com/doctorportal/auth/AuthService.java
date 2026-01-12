package com.doctorportal.auth;

import com.doctorportal.auth.dto.LoginRequest;
import com.doctorportal.auth.dto.LoginResponse;
import com.doctorportal.auth.dto.RegisterRequest;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.UnauthorizedException;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Transactional
	public Long registerPatient(RegisterRequest req) {
		userRepository.findByEmail(req.email()).ifPresent(u -> {
			throw new BadRequestException("Email already registered");
		});

		UserEntity u = new UserEntity();
		u.setName(req.name());
		u.setEmail(req.email().toLowerCase());
		u.setPassword(passwordEncoder.encode(req.password()));
		u.setRole(Role.PATIENT);
		u.setPhone(req.phone());
		u.setGender(req.gender());
		u.setDateOfBirth(req.dateOfBirth());
		u.setHospital(null);
		u.setActive(true);
		return userRepository.save(u).getId();
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest req) {
		UserEntity user = userRepository.findByEmail(req.email().toLowerCase())
				.orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

		if (!user.isActive()) {
			throw new UnauthorizedException("User is inactive");
		}

		if (!passwordEncoder.matches(req.password(), user.getPassword())) {
			throw new UnauthorizedException("Invalid email or password");
		}

		Long hospitalId = user.getHospital() != null ? user.getHospital().getId() : null;
		return new LoginResponse(user.getId(), user.getRole(), hospitalId);
	}
}

