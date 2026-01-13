package com.doctorportal.department.dto;

public record DepartmentResponse(
		Long id,
		Long hospitalId,
		String name,
		String description,
		boolean active
) {
}
