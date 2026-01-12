package com.doctorportal.common.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error payload for all REST APIs.
 * Keep it small and consistent for frontend handling.
 */
public record ApiErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path,
		Map<String, String> fieldErrors
) {
}

