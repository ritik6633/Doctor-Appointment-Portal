package com.doctorportal.controller;

import com.doctorportal.hospital.HospitalController;
import com.doctorportal.hospital.HospitalService;
import com.doctorportal.hospital.dto.CreateHospitalRequest;
import com.doctorportal.hospital.dto.HospitalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HospitalController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class HospitalControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	HospitalService hospitalService;

	@Test
	void list_returnsHospitals() throws Exception {
		Mockito.when(hospitalService.listHospitals(false)).thenReturn(List.of(
				new HospitalResponse(1L, "City Hospital", "Pune", "Address", "c@h.com", "999", true, true)
		));

		mvc.perform(get("/hospitals"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(1)));
	}

	@Test
	void create_validRequest_returnsHospital() throws Exception {
		CreateHospitalRequest req = new CreateHospitalRequest("New Hospital", "Pune", "Addr", "a@b.com", "999");
		Mockito.when(hospitalService.createHospital(Mockito.any())).thenReturn(
				new HospitalResponse(10L, req.name(), req.city(), req.address(), req.contactEmail(), req.contactPhone(), false, true)
		);

		mvc.perform(post("/hospitals")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(10)))
				.andExpect(jsonPath("$.approved", is(false)))
				.andExpect(jsonPath("$.active", is(true)));
	}

	@Test
	void approve_callsService() throws Exception {
		Mockito.when(hospitalService.approveHospital(1L)).thenReturn(
				new HospitalResponse(1L, "H", "C", "A", "e@e.com", "99", true, true)
		);

		mvc.perform(put("/hospitals/1/approve"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.approved", is(true)));
	}

	@Test
	void setActive_callsService() throws Exception {
		Mockito.when(hospitalService.setActive(1L, false)).thenReturn(
				new HospitalResponse(1L, "H", "C", "A", "e@e.com", "99", true, false)
		);

		mvc.perform(put("/hospitals/1/active")
						.param("value", "false"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.active", is(false)));
	}

	@Test
	void create_validationError_returns400() throws Exception {
		var bad = new CreateHospitalRequest("", "", "", "notEmail", "");
		mvc.perform(post("/hospitals")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(bad)))
				.andExpect(status().isBadRequest());
	}
}

