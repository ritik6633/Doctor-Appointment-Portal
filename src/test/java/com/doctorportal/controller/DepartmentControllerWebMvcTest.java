package com.doctorportal.controller;

import com.doctorportal.department.DepartmentController;
import com.doctorportal.department.DepartmentService;
import com.doctorportal.department.dto.CreateDepartmentRequest;
import com.doctorportal.department.dto.DepartmentResponse;
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

@WebMvcTest(controllers = DepartmentController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class DepartmentControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	DepartmentService departmentService;

	@Test
	void listByHospital_returnsDepartments() throws Exception {
		Mockito.when(departmentService.listByHospital(1L)).thenReturn(List.of(
				new DepartmentResponse(1L, 1L, "Cardiology", "", true)
		));

		mvc.perform(get("/departments/hospital/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is("Cardiology")));
	}

	@Test
	void create_validRequest_returnsDepartment() throws Exception {
		CreateDepartmentRequest req = new CreateDepartmentRequest(1L, "ENT", "Ear");
		Mockito.when(departmentService.create(Mockito.any())).thenReturn(
				new DepartmentResponse(10L, 1L, "ENT", "Ear", true)
		);

		mvc.perform(post("/departments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(10)));
	}

	@Test
	void create_validationError_returns400() throws Exception {
		var bad = new CreateDepartmentRequest(null, "", "");
		mvc.perform(post("/departments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(bad)))
				.andExpect(status().isBadRequest());
	}
}

