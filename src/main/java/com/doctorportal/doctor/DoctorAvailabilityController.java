package com.doctorportal.doctor;

import com.doctorportal.doctor.dto.DoctorAvailabilityResponse;
import com.doctorportal.doctor.dto.UpsertAvailabilityRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorAvailabilityController {
	private final DoctorAvailabilityService availabilityService;

	@GetMapping("/{doctorId}/availability")
	public ResponseEntity<List<DoctorAvailabilityResponse>> list(@PathVariable Long doctorId) {
		return ResponseEntity.ok(availabilityService.listByDoctor(doctorId));
	}

	@PutMapping("/{doctorId}/availability")
	public ResponseEntity<DoctorAvailabilityResponse> upsert(
			@PathVariable Long doctorId,
			@Valid @RequestBody UpsertAvailabilityRequest req
	) {
		return ResponseEntity.ok(availabilityService.upsert(doctorId, req));
	}
}

