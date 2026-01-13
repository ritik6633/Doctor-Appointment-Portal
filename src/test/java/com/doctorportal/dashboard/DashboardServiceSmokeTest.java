package com.doctorportal.dashboard;

import com.doctorportal.auth.AuthContext;
import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.user.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DashboardServiceSmokeTest {

	@Autowired
	DashboardService dashboardService;

	@AfterEach
	void cleanup() {
		AuthContext.clear();
	}

	@Test
	void hospitalAdminDashboardBuilds() {
		AuthContext.set(new AuthPrincipal(2L, Role.HOSPITAL_ADMIN));
		assertNotNull(dashboardService.hospitalAdminDashboard());
	}

	@Test
	void developerAdminDashboardBuilds() {
		AuthContext.set(new AuthPrincipal(1L, Role.DEVELOPER_ADMIN));
		assertNotNull(dashboardService.developerAdminDashboard());
	}
}

