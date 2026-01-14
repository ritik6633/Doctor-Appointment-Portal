package com.doctorportal.user.dto;

import java.time.LocalDate;

public record HospitalAdminUserResponse(
		Long userId,
		String name,
		String email,
		String phone,
		String gender,
		LocalDate dateOfBirth,
		Long hospitalId,
		String hospitalName,
		boolean active
) {
}
