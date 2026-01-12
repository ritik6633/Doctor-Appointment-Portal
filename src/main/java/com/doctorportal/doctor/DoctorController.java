package com.doctorportal.doctor;

import com.doctorportal.doctor.dto.CreateDoctorRequest;
import com.doctorportal.doctor.dto.DoctorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
	private final DoctorService doctorService;

	@PostMapping
	public ResponseEntity<DoctorResponse> create(@Valid @RequestBody CreateDoctorRequest req) {
		return ResponseEntity.ok(doctorService.create(req));
	}

	@GetMapping("/hospital/{id}")
	public ResponseEntity<List<DoctorResponse>> listByHospital(@PathVariable("id") Long hospitalId) {
		return ResponseEntity.ok(doctorService.listByHospital(hospitalId));
	}
}

