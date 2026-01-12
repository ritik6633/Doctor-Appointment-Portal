package com.doctorportal.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Hospital admin creates a doctor by creating both:
 * - a User row (role=DOCTOR)
 * - a Doctor row
 */
public record CreateDoctorRequest(
		@NotNull Long hospitalId,
		@NotNull Long departmentId,

		@NotBlank String name,
		@Email @NotBlank String email,
		@NotBlank String password,
		String phone,
		String gender,
		LocalDate dateOfBirth,

		@NotBlank String specialization,
		String qualification,
		Integer experienceYears,
		@NotNull BigDecimal consultationFee
) {
}

