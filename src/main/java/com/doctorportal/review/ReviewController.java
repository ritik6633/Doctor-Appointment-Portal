package com.doctorportal.review;

import com.doctorportal.review.dto.CreateReviewRequest;
import com.doctorportal.review.dto.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<ReviewResponse> create(@Valid @RequestBody CreateReviewRequest req) {
		return ResponseEntity.ok(reviewService.create(req));
	}

	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<ReviewResponse>> listByDoctor(@PathVariable Long doctorId) {
		return ResponseEntity.ok(reviewService.listByDoctor(doctorId));
	}
}

