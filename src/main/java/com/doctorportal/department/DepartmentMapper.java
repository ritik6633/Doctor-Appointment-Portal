package com.doctorportal.department;

import com.doctorportal.department.dto.DepartmentResponse;

public final class DepartmentMapper {
	private DepartmentMapper() {
	}

	public static DepartmentResponse toResponse(DepartmentEntity e) {
		return new DepartmentResponse(
			e.getId(),
			e.getHospital().getId(),
			e.getName(),
			e.getDescription()
		);
	}
}

