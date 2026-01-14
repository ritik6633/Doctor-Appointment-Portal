package com.doctorportal.user;

import com.doctorportal.user.dto.CreateHospitalAdminRequest;
import com.doctorportal.user.dto.CreateHospitalAdminResponse;
import com.doctorportal.user.dto.HospitalAdminUserResponse;
import com.doctorportal.user.dto.ResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Platform administration endpoints.
 * Authentication is handled by HeaderAuthFilter (X-USER-ID/X-ROLE).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {
	private final UserAdminService userAdminService;

	@PostMapping("/hospital-admin")
	public ResponseEntity<CreateHospitalAdminResponse> createHospitalAdmin(@Valid @RequestBody CreateHospitalAdminRequest req) {
		return ResponseEntity.ok(userAdminService.createHospitalAdmin(req));
	}

	@GetMapping("/hospital-admin")
	public ResponseEntity<List<HospitalAdminUserResponse>> listHospitalAdmins() {
		return ResponseEntity.ok(userAdminService.listHospitalAdmins());
	}

	@PutMapping("/hospital-admin/{userId}/active")
	public ResponseEntity<HospitalAdminUserResponse> setHospitalAdminActive(
			@PathVariable Long userId,
			@RequestParam("value") boolean active
	) {
		return ResponseEntity.ok(userAdminService.setHospitalAdminActive(userId, active));
	}

	@PutMapping("/hospital-admin/{userId}/password")
	public ResponseEntity<Void> resetHospitalAdminPassword(
			@PathVariable Long userId,
			@Valid @RequestBody ResetPasswordRequest req
	) {
		userAdminService.resetHospitalAdminPassword(userId, req);
		return ResponseEntity.noContent().build();
	}
}
