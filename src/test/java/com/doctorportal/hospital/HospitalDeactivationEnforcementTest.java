package com.doctorportal.hospital;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.appointment.AppointmentService;
import com.doctorportal.appointment.dto.BookAppointmentRequest;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class HospitalDeactivationEnforcementTest {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	AppointmentService appointmentService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void bookingRejectedWhenHospitalDeactivated() {
		// Deactivate hospital as developer admin
		AuthContext.set(new AuthPrincipal(1L, Role.DEVELOPER_ADMIN));
		hospitalService.setActive(1L, false);

		// Patient tries to book with a doctor from that hospital
		AuthContext.set(new AuthPrincipal(5L, Role.PATIENT));
		var req = new BookAppointmentRequest(
				1L,
				LocalDate.now().plusDays(1),
				LocalTime.of(10, 0),
				"Test"
		);

		assertThrows(BadRequestException.class, () -> appointmentService.book(req));
	}
}

