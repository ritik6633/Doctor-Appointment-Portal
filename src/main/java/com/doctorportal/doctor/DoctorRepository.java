package com.doctorportal.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
	List<DoctorEntity> findByHospitalId(Long hospitalId);
	List<DoctorEntity> findByHospitalIdAndDepartmentId(Long hospitalId, Long departmentId);
	Optional<DoctorEntity> findByUserId(Long userId);
}
