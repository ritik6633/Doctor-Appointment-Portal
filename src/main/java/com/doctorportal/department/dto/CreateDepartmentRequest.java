package com.doctorportal.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDepartmentRequest(
		@NotNull Long hospitalId,
		@NotBlank String name,
		String description
) {
}

