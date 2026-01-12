package com.doctorportal.appointment;

import com.doctorportal.doctor.DoctorEntity;
import com.doctorportal.hospital.HospitalEntity;
import com.doctorportal.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class AppointmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", nullable = false)
	private DoctorEntity doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", nullable = false)
	private UserEntity patient;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", nullable = false)
	private HospitalEntity hospital;

	@Column(name = "appointment_date", nullable = false)
	private LocalDate appointmentDate;

	@Column(name = "appointment_time", nullable = false)
	private LocalTime appointmentTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus status;

	@Column(length = 1000)
	private String symptoms;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@PrePersist
	void prePersist() {
		createdAt = Instant.now();
	}
}

