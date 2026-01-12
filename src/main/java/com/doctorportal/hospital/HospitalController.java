package com.doctorportal.hospital;

import com.doctorportal.hospital.dto.CreateHospitalRequest;
import com.doctorportal.hospital.dto.HospitalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospitals")
public class HospitalController {
	private final HospitalService hospitalService;

	@GetMapping
	public ResponseEntity<List<HospitalResponse>> list() {
		// Public: show only approved hospitals
		return ResponseEntity.ok(hospitalService.listHospitals(false));
	}

	@PostMapping
	public ResponseEntity<HospitalResponse> create(@Valid @RequestBody CreateHospitalRequest req) {
		return ResponseEntity.ok(hospitalService.createHospital(req));
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<HospitalResponse> approve(@PathVariable Long id) {
		return ResponseEntity.ok(hospitalService.approveHospital(id));
	}
}

