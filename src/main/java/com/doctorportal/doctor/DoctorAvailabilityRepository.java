package com.doctorportal.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailabilityEntity, Long> {
	List<DoctorAvailabilityEntity> findByDoctorId(Long doctorId);
	List<DoctorAvailabilityEntity> findByDoctorIdOrderByDayOfWeekAscStartTimeAsc(Long doctorId);
	List<DoctorAvailabilityEntity> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}

