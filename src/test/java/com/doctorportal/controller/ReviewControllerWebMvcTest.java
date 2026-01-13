package com.doctorportal.controller;

import com.doctorportal.review.ReviewController;
import com.doctorportal.review.ReviewService;
import com.doctorportal.review.dto.CreateReviewRequest;
import com.doctorportal.review.dto.ReviewResponse;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
@Import(com.doctorportal.common.exception.GlobalExceptionHandler.class)
class ReviewControllerWebMvcTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	ReviewService reviewService;

	@Test
	void listByDoctor_returnsReviews() throws Exception {
		Mockito.when(reviewService.listByDoctor(1L)).thenReturn(List.of(
				new ReviewResponse(1L, 1L, 5L, "Patient", 5, "Good", Instant.now())
		));

		mvc.perform(get("/reviews/doctor/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].rating", is(5)));
	}

	@Test
	void create_returnsReview() throws Exception {
		CreateReviewRequest req = new CreateReviewRequest(1L, 5, "Nice");
		Mockito.when(reviewService.create(Mockito.any())).thenReturn(
				new ReviewResponse(10L, 1L, 5L, "Patient", 5, "Nice", Instant.now())
		);

		mvc.perform(post("/reviews")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(10)));
	}

	@Test
	void create_validationError_returns400() throws Exception {
		mvc.perform(post("/reviews")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}
