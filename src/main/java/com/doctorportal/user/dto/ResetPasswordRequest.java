package com.doctorportal.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Admin reset password request.
 */
public record ResetPasswordRequest(
		@NotBlank String newPassword
) {
}

