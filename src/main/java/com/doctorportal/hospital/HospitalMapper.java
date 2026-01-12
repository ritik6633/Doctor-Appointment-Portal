package com.doctorportal.hospital;

import com.doctorportal.hospital.dto.HospitalResponse;

public final class HospitalMapper {
	private HospitalMapper() {
	}

	public static HospitalResponse toResponse(HospitalEntity e) {
		return new HospitalResponse(
			e.getId(),
			e.getName(),
			e.getCity(),
			e.getAddress(),
			e.getContactEmail(),
			e.getContactPhone(),
			e.isApproved(),
			e.isActive()
		);
	}
}

