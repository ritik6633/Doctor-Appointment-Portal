package com.doctorportal.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterRequest(
		@NotBlank String name,
		@Email @NotBlank String email,
		@NotBlank String password,
		String phone,
		String gender,
		@NotNull LocalDate dateOfBirth
) {
}

