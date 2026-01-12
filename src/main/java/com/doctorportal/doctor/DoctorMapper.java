package com.doctorportal.doctor;

import com.doctorportal.doctor.dto.DoctorResponse;

public final class DoctorMapper {
	private DoctorMapper() {
	}

	public static DoctorResponse toResponse(DoctorEntity e) {
		return new DoctorResponse(
			e.getId(),
			e.getUser().getId(),
			e.getUser().getName(),
			e.getUser().getEmail(),
			e.getHospital().getId(),
			e.getDepartment().getId(),
			e.getDepartment().getName(),
			e.getSpecialization(),
			e.getQualification(),
			e.getExperienceYears(),
			e.getConsultationFee(),
			e.isActive()
		);
	}
}

