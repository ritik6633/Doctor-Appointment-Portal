package com.doctorportal.department;

import com.doctorportal.department.dto.CreateDepartmentRequest;
import com.doctorportal.department.dto.DepartmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
	private final DepartmentService departmentService;

	@PostMapping
	public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody CreateDepartmentRequest req) {
		return ResponseEntity.ok(departmentService.create(req));
	}

	@GetMapping("/hospital/{hospitalId}")
	public ResponseEntity<List<DepartmentResponse>> listByHospital(@PathVariable Long hospitalId) {
		return ResponseEntity.ok(departmentService.listByHospital(hospitalId));
	}
}
