package com.doctorportal.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
	List<ReviewEntity> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
	boolean existsByDoctorIdAndPatientId(Long doctorId, Long patientId);
}

