package com.doctorportal.hospital;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.appointment.AppointmentService;
import com.doctorportal.appointment.dto.BookAppointmentRequest;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HospitalReactivationHappyPathTest {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	AppointmentService appointmentService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void bookingWorksAgainAfterHospitalReactivated() {
		AuthContext.set(new AuthPrincipal(1L, Role.DEVELOPER_ADMIN));
		hospitalService.setActive(1L, false);
		hospitalService.setActive(1L, true);

		AuthContext.set(new AuthPrincipal(5L, Role.PATIENT));

		// Seed availability for doctorId=1 is MONDAY/WEDNESDAY 10:00-13:00 with 15-min slots.
		LocalDate date = nextDayOfWeek(LocalDate.now().plusDays(1), DayOfWeek.MONDAY);

		var req = new BookAppointmentRequest(
				1L,
				date,
				LocalTime.of(10, 0),
				"Reactivation booking"
		);

		assertNotNull(appointmentService.book(req));
	}

	private static LocalDate nextDayOfWeek(LocalDate startInclusive, DayOfWeek target) {
		LocalDate d = startInclusive;
		for (int i = 0; i < 14; i++) {
			if (d.getDayOfWeek() == target) {
				return d;
			}
			d = d.plusDays(1);
		}
		throw new IllegalStateException("Could not find target day within 14 days");
	}
}
