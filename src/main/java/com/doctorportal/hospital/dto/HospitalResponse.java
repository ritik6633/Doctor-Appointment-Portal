package com.doctorportal.hospital.dto;

public record HospitalResponse(
		Long id,
		String name,
		String city,
		String address,
		String contactEmail,
		String contactPhone,
		boolean approved,
		boolean active
) {
}

