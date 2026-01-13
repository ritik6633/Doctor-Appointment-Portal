package com.doctorportal.controller;

import com.doctorportal.availability.AvailabilityController;
import com.doctorportal.availability.AvailabilityService;
import com.doctorportal.availability.AvailableSlotResponse;
import com.doctorportal.doctor.DoctorAvailabilityService;
import com.doctorportal.doctor.dto.DoctorAvailabilityResponse;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AvailabilityController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class AvailabilityControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	AvailabilityService availabilityService;

	@MockBean
	DoctorAvailabilityService doctorAvailabilityService;

	@Test
	void listDoctorSlots_returnsSlots() throws Exception {
		Mockito.when(availabilityService.listAvailableSlots(Mockito.eq(1L), Mockito.any(), Mockito.eq(7)))
				.thenReturn(List.of(new AvailableSlotResponse(LocalDate.now(), LocalTime.of(10, 0))));

		mvc.perform(get("/availability/doctor/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].time", is("10:00:00")));
	}

	@Test
	void upsertWeeklyAvailability_callsService() throws Exception {
		String json = "{\"doctorId\":1,\"dayOfWeek\":\"MONDAY\",\"startTime\":\"10:00:00\",\"endTime\":\"12:00:00\",\"slotDurationMinutes\":15}";
		Mockito.when(doctorAvailabilityService.upsert(Mockito.eq(1L), Mockito.any()))
				.thenReturn(new DoctorAvailabilityResponse(1L, 1L, DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), 15));

		mvc.perform(post("/availability")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dayOfWeek", is("MONDAY")));
	}

	@Test
	void upsertWeeklyAvailability_validationError_returns400() throws Exception {
		mvc.perform(post("/availability")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}

