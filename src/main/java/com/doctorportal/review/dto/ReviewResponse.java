package com.doctorportal.review.dto;

import java.time.Instant;

public record ReviewResponse(
		Long id,
		Long doctorId,
		Long patientId,
		String patientName,
		int rating,
		String comment,
		Instant createdAt
) {
}

