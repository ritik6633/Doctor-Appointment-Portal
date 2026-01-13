package com.doctorportal.appointment;

import com.doctorportal.appointment.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
	private final AppointmentService appointmentService;

	@PostMapping
	public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody BookAppointmentRequest req) {
		return ResponseEntity.ok(appointmentService.book(req));
	}

	@GetMapping("/patient/{id}")
	public ResponseEntity<List<AppointmentResponse>> listForPatient(@PathVariable("id") Long patientId) {
		return ResponseEntity.ok(appointmentService.listForPatient(patientId));
	}

	@GetMapping("/doctor/{id}")
	public ResponseEntity<List<AppointmentResponse>> listForDoctor(@PathVariable("id") Long doctorUserId) {
		return ResponseEntity.ok(appointmentService.listForDoctor(doctorUserId));
	}

	@GetMapping("/hospital/{id}")
	public ResponseEntity<List<HospitalAppointmentResponse>> listForHospital(@PathVariable("id") Long hospitalId) {
		return ResponseEntity.ok(appointmentService.listForHospital(hospitalId));
	}

	@GetMapping("/hospital/{id}/search")
	public ResponseEntity<List<HospitalAppointmentResponse>> searchForHospital(
			@PathVariable("id") Long hospitalId,
			@RequestParam(value = "from", required = false) LocalDate from,
			@RequestParam(value = "to", required = false) LocalDate to,
			@RequestParam(value = "status", required = false) AppointmentStatus status
	) {
		return ResponseEntity.ok(appointmentService.searchForHospital(
				hospitalId,
				new HospitalAppointmentSearchRequest(from, to, status)
		));
	}

	@GetMapping("/hospital/{id}/audit")
	public ResponseEntity<List<DeveloperHospitalAppointmentResponse>> auditHospitalAppointments(
			@PathVariable("id") Long hospitalId
	) {
		return ResponseEntity.ok(appointmentService.developerAuditHospitalAppointments(hospitalId));
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<AppointmentResponse> cancel(@PathVariable("id") Long appointmentId) {
		return ResponseEntity.ok(appointmentService.cancel(appointmentId));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<AppointmentResponse> updateStatus(
			@PathVariable("id") Long appointmentId,
			@Valid @RequestBody UpdateAppointmentStatusRequest req
	) {
		return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, req));
	}
}
