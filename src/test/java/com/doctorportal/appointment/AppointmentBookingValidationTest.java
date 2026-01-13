package com.doctorportal.appointment;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
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
class AppointmentBookingValidationTest {

	@Autowired
	AppointmentService appointmentService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void bookingRejectedWhenNotAlignedToSlotGrid() {
		// patient id 5 exists in seed data
		AuthContext.set(new AuthPrincipal(5L, Role.PATIENT));

		// Doctor 1 has 15-min slots starting at 10:00 in seed
		var req = new BookAppointmentRequest(
				1L,
				LocalDate.now().plusDays(1),
				LocalTime.of(10, 7),
				"Test symptoms"
		);

		assertThrows(BadRequestException.class, () -> appointmentService.book(req));
	}
}

