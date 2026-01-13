package com.doctorportal.availability;

import com.doctorportal.doctor.DoctorAvailabilityService;
import com.doctorportal.doctor.dto.DoctorAvailabilityResponse;
import com.doctorportal.doctor.dto.UpsertAvailabilityRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Availability endpoints.
 *
 * - GET /availability/doctor/{id}: public (patients use it to see bookable slots)
 * - POST /availability: doctor/hospital-admin can define weekly availability
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/availability")
public class AvailabilityController {
	private final AvailabilityService availabilityService;
	private final DoctorAvailabilityService doctorAvailabilityService;

	@GetMapping("/doctor/{id}")
	public ResponseEntity<List<AvailableSlotResponse>> listDoctorSlots(
			@org.springframework.web.bind.annotation.PathVariable("id") Long doctorId,
			@RequestParam(value = "from", required = false) LocalDate from,
			@RequestParam(value = "days", required = false, defaultValue = "7") int days
	) {
		return ResponseEntity.ok(availabilityService.listAvailableSlots(doctorId, from, days));
	}

	@PostMapping
	public ResponseEntity<DoctorAvailabilityResponse> upsertWeeklyAvailability(
			@Valid @RequestBody UpsertAvailabilityForDoctorRequest req
	) {
		UpsertAvailabilityRequest internal = new UpsertAvailabilityRequest(
				req.dayOfWeek(),
				req.startTime(),
				req.endTime(),
				req.slotDurationMinutes()
		);
		return ResponseEntity.ok(doctorAvailabilityService.upsert(req.doctorId(), internal));
	}
}
