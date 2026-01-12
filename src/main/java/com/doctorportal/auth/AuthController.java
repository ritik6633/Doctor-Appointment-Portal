package com.doctorportal.auth;

import com.doctorportal.auth.dto.LoginRequest;
import com.doctorportal.auth.dto.LoginResponse;
import com.doctorportal.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest req) {
		Long userId = authService.registerPatient(req);
		return ResponseEntity.ok(Map.of("userId", userId));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
		return ResponseEntity.ok(authService.login(req));
	}
}

