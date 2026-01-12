package com.doctorportal.review;

import com.doctorportal.review.dto.ReviewResponse;

public final class ReviewMapper {
	private ReviewMapper() {
	}

	public static ReviewResponse toResponse(ReviewEntity e) {
		return new ReviewResponse(
			e.getId(),
			e.getDoctor().getId(),
			e.getPatient().getId(),
			e.getPatient().getName(),
			e.getRating(),
			e.getComment(),
			e.getCreatedAt()
		);
	}
}

