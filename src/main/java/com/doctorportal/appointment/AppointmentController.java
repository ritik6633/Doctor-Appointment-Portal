package com.doctorportal.appointment;

import com.doctorportal.appointment.dto.AppointmentResponse;
import com.doctorportal.appointment.dto.BookAppointmentRequest;
import com.doctorportal.appointment.dto.UpdateAppointmentStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

