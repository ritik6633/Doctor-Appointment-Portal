package com.doctorportal.controller;

import com.doctorportal.common.exception.GlobalExceptionHandler;
import com.doctorportal.user.UserAdminController;
import com.doctorportal.user.UserAdminService;
import com.doctorportal.user.dto.CreateHospitalAdminRequest;
import com.doctorportal.user.dto.CreateHospitalAdminResponse;
import com.doctorportal.user.dto.HospitalAdminUserResponse;
import com.doctorportal.user.dto.ResetPasswordRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAdminController.class)
@Import(GlobalExceptionHandler.class)
class UserAdminControllerWebMvcTest {
	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	UserAdminService userAdminService;

	@Test
	void createHospitalAdmin_returnsUserId() throws Exception {
		CreateHospitalAdminRequest req = new CreateHospitalAdminRequest(
				1L,
				"Admin",
				"admin@hosp.com",
				"Password@123",
				null,
				null,
				LocalDate.parse("1990-01-01")
		);

		Mockito.when(userAdminService.createHospitalAdmin(Mockito.any()))
				.thenReturn(new CreateHospitalAdminResponse(99L));

		mvc.perform(post("/admin/users/hospital-admin")
						.header("X-USER-ID", "1")
						.header("X-ROLE", "DEVELOPER_ADMIN")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId", is(99)));
	}

	@Test
	void listHospitalAdmins_returnsList() throws Exception {
		Mockito.when(userAdminService.listHospitalAdmins()).thenReturn(List.of(
				new HospitalAdminUserResponse(2L, "Hospital Admin", "admin@h.com", null, null, null, 1L, "CityCare", true)
		));

		mvc.perform(get("/admin/users/hospital-admin")
						.header("X-USER-ID", "1")
						.header("X-ROLE", "DEVELOPER_ADMIN"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].userId", is(2)));
	}

	@Test
	void setHospitalAdminActive_updatesAndReturnsRow() throws Exception {
		Mockito.when(userAdminService.setHospitalAdminActive(2L, false))
				.thenReturn(new HospitalAdminUserResponse(2L, "Hospital Admin", "admin@h.com", null, null, null, 1L, "CityCare", false));

		mvc.perform(put("/admin/users/hospital-admin/2/active")
						.queryParam("value", "false")
						.header("X-USER-ID", "1")
						.header("X-ROLE", "DEVELOPER_ADMIN"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.active", is(false)));
	}

	@Test
	void resetHospitalAdminPassword_returns204() throws Exception {
		ResetPasswordRequest req = new ResetPasswordRequest("New@123");

		mvc.perform(put("/admin/users/hospital-admin/2/password")
						.header("X-USER-ID", "1")
						.header("X-ROLE", "DEVELOPER_ADMIN")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isNoContent());
	}
}
