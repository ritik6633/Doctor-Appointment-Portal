package com.doctorportal.dashboard;

import com.doctorportal.dashboard.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
	private final DashboardService dashboardService;

	@GetMapping("/patient")
	public ResponseEntity<PatientDashboardResponse> patient() {
		return ResponseEntity.ok(dashboardService.patientDashboard());
	}

	@GetMapping("/doctor")
	public ResponseEntity<DoctorDashboardResponse> doctor() {
		return ResponseEntity.ok(dashboardService.doctorDashboard());
	}

	@GetMapping("/hospital-admin")
	public ResponseEntity<HospitalAdminDashboardResponse> hospitalAdmin() {
		return ResponseEntity.ok(dashboardService.hospitalAdminDashboard());
	}

	@GetMapping("/developer-admin")
	public ResponseEntity<DeveloperAdminDashboardResponse> developerAdmin() {
		return ResponseEntity.ok(dashboardService.developerAdminDashboard());
	}
}

