package com.doctorportal.doctor;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.common.exception.ForbiddenException;
import com.doctorportal.doctor.dto.UpsertAvailabilityRequest;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AvailabilityUpsertSecurityTest {

	@Autowired
	DoctorAvailabilityService doctorAvailabilityService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void doctorCannotManageAnotherDoctorsAvailability() {
		// Seed doctor userId=3 maps to doctorId=1
		AuthContext.set(new AuthPrincipal(3L, Role.DOCTOR));

		var req = new UpsertAvailabilityRequest(
				DayOfWeek.MONDAY,
				LocalTime.of(9, 0),
				LocalTime.of(10, 0),
				15
		);

		// doctorId=2 belongs to userId=4 -> should be forbidden
		assertThrows(ForbiddenException.class, () -> doctorAvailabilityService.upsert(2L, req));
	}
}

