package com.doctorportal.auth.dto;

import com.doctorportal.user.Role;

public record LoginResponse(
		Long userId,
		Role role,
		Long hospitalId
) {
}

