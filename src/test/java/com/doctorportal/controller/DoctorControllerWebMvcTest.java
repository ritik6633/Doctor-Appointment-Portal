package com.doctorportal.controller;

import com.doctorportal.doctor.DoctorController;
import com.doctorportal.doctor.DoctorService;
import com.doctorportal.doctor.dto.CreateDoctorRequest;
import com.doctorportal.doctor.dto.DoctorDashboardDetailResponse;
import com.doctorportal.doctor.dto.DoctorResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class DoctorControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	DoctorService doctorService;

	@Test
	void listByHospital_withoutDepartment_returnsList() throws Exception {
		Mockito.when(doctorService.listByHospital(1L)).thenReturn(List.of(
				new DoctorResponse(
						1L,
						11L,
						"Dr A",
						"dr.a@example.com",
						1L,
						1L,
						"Cardiology",
						"Cardiology",
						"MBBS",
						5,
						new BigDecimal("500.00"),
						true
				)
		));

		mvc.perform(get("/doctors/hospital/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is("Dr A")));
	}

	@Test
	void listByHospital_withDepartment_returnsList() throws Exception {
		Mockito.when(doctorService.listByHospitalAndDepartment(1L, 2L)).thenReturn(List.of(
				new DoctorResponse(
						2L,
						22L,
						"Dr B",
						"dr.b@example.com",
						1L,
						2L,
						"ENT",
						"ENT",
						"MBBS",
						3,
						new BigDecimal("300.00"),
						true
				)
		));

		mvc.perform(get("/doctors/hospital/1")
						.param("departmentId", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is("Dr B")));
	}

	@Test
	void myDashboard_returnsDetail() throws Exception {
		DoctorDashboardDetailResponse resp = new DoctorDashboardDetailResponse(
				10, 1, 2, 0,
				List.of(new DoctorDashboardDetailResponse.TodayAppointment(1L, 5L, "Patient", LocalDate.now(), LocalTime.of(10, 0), com.doctorportal.appointment.AppointmentStatus.BOOKED, "") ),
				List.of(new DoctorDashboardDetailResponse.WeeklyAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), 15))
		);
		Mockito.when(doctorService.doctorDashboardDetail()).thenReturn(resp);

		mvc.perform(get("/doctors/me/dashboard"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.todaysAppointments", is(1)));
	}

	@Test
	void create_validationError_returns400() throws Exception {
		// send empty json to trigger validation
		mvc.perform(post("/doctors")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_callsService() throws Exception {
		String json = "{\"hospitalId\":1,\"departmentId\":1,\"name\":\"Dr New\",\"email\":\"dr.new@example.com\",\"password\":\"Password@123\",\"phone\":\"999\",\"gender\":\"MALE\",\"dateOfBirth\":\"1990-01-01\",\"specialization\":\"Cardiology\",\"qualification\":\"MBBS\",\"experienceYears\":5,\"consultationFee\":500}";
		Mockito.when(doctorService.create(Mockito.any(CreateDoctorRequest.class)))
				.thenReturn(new DoctorResponse(
						10L,
						99L,
						"Dr New",
						"dr.new@example.com",
						1L,
						1L,
						"Cardiology",
						"Cardiology",
						"MBBS",
						5,
						new BigDecimal("500.00"),
						true
				));

		mvc.perform(post("/doctors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Dr New")));
	}
}

