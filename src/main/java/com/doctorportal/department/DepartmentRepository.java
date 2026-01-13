package com.doctorportal.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
	List<DepartmentEntity> findByHospitalId(Long hospitalId);
	List<DepartmentEntity> findByHospitalIdAndActiveTrue(Long hospitalId);
}
