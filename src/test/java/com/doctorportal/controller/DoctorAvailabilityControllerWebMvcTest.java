package com.doctorportal.controller;

import com.doctorportal.doctor.DoctorAvailabilityController;
import com.doctorportal.doctor.DoctorAvailabilityService;
import com.doctorportal.doctor.dto.DoctorAvailabilityResponse;
import com.doctorportal.doctor.dto.UpsertAvailabilityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorAvailabilityController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class DoctorAvailabilityControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	DoctorAvailabilityService availabilityService;

	@Test
	void list_returnsAvailabilities() throws Exception {
		Mockito.when(availabilityService.listByDoctor(1L)).thenReturn(List.of(
				new DoctorAvailabilityResponse(1L, 1L, DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), 15)
		));

		mvc.perform(get("/doctors/1/availability"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].dayOfWeek", is("MONDAY")));
	}

	@Test
	void upsert_callsService() throws Exception {
		UpsertAvailabilityRequest req = new UpsertAvailabilityRequest(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), 15);
		Mockito.when(availabilityService.upsert(Mockito.eq(1L), Mockito.any())).thenReturn(
				new DoctorAvailabilityResponse(1L, 1L, DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), 15)
		);

		mvc.perform(put("/doctors/1/availability")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.slotDurationMinutes", is(15)));
	}

	@Test
	void upsert_validationError_returns400() throws Exception {
		// Missing fields
		mvc.perform(put("/doctors/1/availability")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}

