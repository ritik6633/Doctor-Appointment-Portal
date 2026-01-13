package com.doctorportal.doctor;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.hospital.HospitalService;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DoctorOperationsBlockedWhenHospitalInactiveTest {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	DoctorService doctorService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void doctorDashboardBlockedWhenHospitalInactive() {
		AuthContext.set(new AuthPrincipal(1L, Role.DEVELOPER_ADMIN));
		hospitalService.setActive(1L, false);

		AuthContext.set(new AuthPrincipal(3L, Role.DOCTOR));
		assertThrows(BadRequestException.class, () -> doctorService.doctorDashboardDetail());
	}
}

