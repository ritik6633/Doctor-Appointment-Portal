package com.doctorportal.doctor.dto;

import java.math.BigDecimal;

public record DoctorResponse(
		Long id,
		Long userId,
		String name,
		String email,
		Long hospitalId,
		Long departmentId,
		String departmentName,
		String specialization,
		String qualification,
		Integer experienceYears,
		BigDecimal consultationFee,
		boolean active
) {
}

