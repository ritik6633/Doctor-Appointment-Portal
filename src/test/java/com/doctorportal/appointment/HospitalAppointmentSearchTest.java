package com.doctorportal.appointment;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.appointment.dto.HospitalAppointmentSearchRequest;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class HospitalAppointmentSearchTest {

	@Autowired
	AppointmentService appointmentService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void invalidDateRangeRejected() {
		AuthContext.set(new AuthPrincipal(2L, Role.HOSPITAL_ADMIN));
		assertThrows(BadRequestException.class, () -> appointmentService.searchForHospital(
				1L,
				new HospitalAppointmentSearchRequest(LocalDate.now(), LocalDate.now().minusDays(1), null)
		));
	}
}

