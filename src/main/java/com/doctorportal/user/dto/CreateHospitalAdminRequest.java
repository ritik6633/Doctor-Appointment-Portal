package com.doctorportal.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Developer admin creates a hospital admin for a specific hospital.
 * Matches business rule: hospital admin registration happens inside developer admin flow.
 */
public record CreateHospitalAdminRequest(
		@NotNull Long hospitalId,
		@NotBlank String name,
		@Email @NotBlank String email,
		@NotBlank String password,
		String phone,
		String gender,
		LocalDate dateOfBirth
) {
}
