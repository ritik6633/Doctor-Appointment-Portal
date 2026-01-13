package com.doctorportal.doctor;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DoctorDashboardDetailTest {

	@Autowired
	DoctorService doctorService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void doctorDashboardDetailBuilds() {
		// Seed doctor userId=3
		AuthContext.set(new AuthPrincipal(3L, Role.DOCTOR));
		assertNotNull(doctorService.doctorDashboardDetail());
	}
}

