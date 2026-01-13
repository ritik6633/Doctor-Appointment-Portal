package com.doctorportal.controller;

import com.doctorportal.auth.AuthController;
import com.doctorportal.auth.AuthService;
import com.doctorportal.auth.dto.LoginRequest;
import com.doctorportal.auth.dto.LoginResponse;
import com.doctorportal.auth.dto.RegisterRequest;
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
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class AuthControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	AuthService authService;

	@Test
	void register_returnsUserId() throws Exception {
		RegisterRequest req = new RegisterRequest(
				"Test Patient",
				"patient.test@example.com",
				"Password@123",
				"9999999999",
				"MALE",
				LocalDate.of(2000, 1, 1)
		);
		Mockito.when(authService.registerPatient(Mockito.any())).thenReturn(99L);

		mvc.perform(post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId", is(99)));
	}

	@Test
	void login_returnsLoginResponse() throws Exception {
		LoginRequest req = new LoginRequest("patient.test@example.com", "Password@123");
		Mockito.when(authService.login(Mockito.any()))
				.thenReturn(new LoginResponse(99L, com.doctorportal.user.Role.PATIENT, null));

		mvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId", is(99)))
				.andExpect(jsonPath("$.role", is("PATIENT")));
	}

	@Test
	void register_validationError_returns400() throws Exception {
		// missing name + invalid email + blank password etc
		Map<String, Object> bad = Map.of(
				"name", "",
				"email", "not-an-email",
				"password", "",
				"phone", "",
				"gender", "MALE",
				"dateOfBirth", "2000-01-01"
		);

		mvc.perform(post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(bad)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Validation failed")));
	}
}

