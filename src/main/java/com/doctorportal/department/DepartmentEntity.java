package com.doctorportal.department;

import com.doctorportal.hospital.HospitalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class DepartmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", nullable = false)
	private HospitalEntity hospital;

	@Column(nullable = false)
	private String name;

	@Column(length = 500)
	private String description;
}

