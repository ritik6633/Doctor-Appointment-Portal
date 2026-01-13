package com.doctorportal.controller;

import com.doctorportal.appointment.AppointmentController;
import com.doctorportal.appointment.AppointmentService;
import com.doctorportal.appointment.AppointmentStatus;
import com.doctorportal.appointment.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppointmentController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class AppointmentControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	AppointmentService appointmentService;

	@Test
	void book_returnsAppointment() throws Exception {
		BookAppointmentRequest req = new BookAppointmentRequest(1L, LocalDate.now().plusDays(1), LocalTime.of(10, 0), "S");
		Mockito.when(appointmentService.book(Mockito.any())).thenReturn(
				new AppointmentResponse(1L, 1L, "Dr", 5L, "Patient", 1L, req.appointmentDate(), req.appointmentTime(), AppointmentStatus.BOOKED, "S", Instant.now())
		);

		mvc.perform(post("/appointments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("BOOKED")));
	}

	@Test
	void listForPatient_returnsList() throws Exception {
		Mockito.when(appointmentService.listForPatient(5L)).thenReturn(List.of());
		mvc.perform(get("/appointments/patient/5"))
				.andExpect(status().isOk());
	}

	@Test
	void listForDoctor_returnsList() throws Exception {
		Mockito.when(appointmentService.listForDoctor(3L)).thenReturn(List.of());
		mvc.perform(get("/appointments/doctor/3"))
				.andExpect(status().isOk());
	}

	@Test
	void searchForHospital_passesParams() throws Exception {
		Mockito.when(appointmentService.searchForHospital(Mockito.eq(1L), Mockito.any())).thenReturn(List.of());
		mvc.perform(get("/appointments/hospital/1/search")
						.param("from", "2026-01-01")
						.param("to", "2026-01-31")
						.param("status", "BOOKED"))
				.andExpect(status().isOk());
	}

	@Test
	void auditHospitalAppointments_returnsList() throws Exception {
		Mockito.when(appointmentService.developerAuditHospitalAppointments(1L)).thenReturn(List.of());
		mvc.perform(get("/appointments/hospital/1/audit"))
				.andExpect(status().isOk());
	}

	@Test
	void cancel_callsService() throws Exception {
		Mockito.when(appointmentService.cancel(1L)).thenReturn(
				new AppointmentResponse(1L, 1L, "Dr", 5L, "Patient", 1L, LocalDate.now().plusDays(1), LocalTime.of(10, 0), AppointmentStatus.CANCELLED, "S", Instant.now())
		);
		mvc.perform(put("/appointments/1/cancel"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("CANCELLED")));
	}

	@Test
	void updateStatus_validationError_returns400() throws Exception {
		mvc.perform(put("/appointments/1/status")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}

