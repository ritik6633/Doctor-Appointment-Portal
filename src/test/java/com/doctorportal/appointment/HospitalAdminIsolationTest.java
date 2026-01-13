package com.doctorportal.appointment;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.common.exception.ForbiddenException;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class HospitalAdminIsolationTest {

	@Autowired
	AppointmentService appointmentService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void hospitalAdminCannotReadOtherHospitalAppointments() {
		// seed hospital admin user is id=2, hospital_id=1
		AuthContext.set(new AuthPrincipal(2L, Role.HOSPITAL_ADMIN));
		assertThrows(ForbiddenException.class, () -> appointmentService.listForHospital(999L));
	}
}

