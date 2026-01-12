package com.doctorportal.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
	List<AppointmentEntity> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(Long patientId);
	List<AppointmentEntity> findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(Long doctorId, LocalDate appointmentDate);
	List<AppointmentEntity> findByDoctorIdOrderByAppointmentDateDescAppointmentTimeDesc(Long doctorId);
	List<AppointmentEntity> findByHospitalIdOrderByAppointmentDateDescAppointmentTimeDesc(Long hospitalId);
	boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(Long doctorId, LocalDate date, java.time.LocalTime time, List<AppointmentStatus> statuses);
}

