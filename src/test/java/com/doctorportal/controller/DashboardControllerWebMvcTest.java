package com.doctorportal.controller;

import com.doctorportal.dashboard.DashboardController;
import com.doctorportal.dashboard.DashboardService;
import com.doctorportal.dashboard.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DashboardController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class DashboardControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	DashboardService dashboardService;

	@Test
	void patientDashboard_endpointWorks() throws Exception {
		Mockito.when(dashboardService.patientDashboard()).thenReturn(new PatientDashboardResponse(1, 1, 0, null));
		mvc.perform(get("/dashboard/patient"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalAppointments", is(1)));
	}

	@Test
	void doctorDashboard_endpointWorks() throws Exception {
		Mockito.when(dashboardService.doctorDashboard()).thenReturn(new DoctorDashboardResponse(1, 0, 0, 0));
		mvc.perform(get("/dashboard/doctor"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalAppointments", is(1)));
	}

	@Test
	void hospitalAdminDashboard_endpointWorks() throws Exception {
		Mockito.when(dashboardService.hospitalAdminDashboard()).thenReturn(
				new HospitalAdminDashboardResponse(1L, 1, 1, 1, 0, 0, List.of(), List.of())
		);
		mvc.perform(get("/dashboard/hospital-admin"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.hospitalId", is(1)));
	}

	@Test
	void developerAdminDashboard_endpointWorks() throws Exception {
		Mockito.when(dashboardService.developerAdminDashboard()).thenReturn(
				new DeveloperAdminDashboardResponse(1, 1, 0, 1, 1, 0, 0, List.of())
		);
		mvc.perform(get("/dashboard/developer-admin"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalHospitals", is(1)));
	}
}

