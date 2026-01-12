package com.doctorportal.hospital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateHospitalRequest(
		@NotBlank String name,
		@NotBlank String city,
		@NotBlank String address,
		@Email @NotBlank String contactEmail,
		@NotBlank String contactPhone
) {
}

