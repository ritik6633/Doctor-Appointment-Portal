package com.doctorportal.department;

import com.doctorportal.auth.RequireRole;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.hospital.HospitalEntity;
import com.doctorportal.hospital.HospitalRepository;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import com.doctorportal.department.dto.CreateDepartmentRequest;
import com.doctorportal.department.dto.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
	private final DepartmentRepository departmentRepository;
	private final HospitalRepository hospitalRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<DepartmentResponse> listByHospital(Long hospitalId) {
		return departmentRepository.findByHospitalId(hospitalId).stream()
				.filter(DepartmentEntity::isActive)
				.map(DepartmentMapper::toResponse)
				.toList();
	}

	@Transactional
	public DepartmentResponse create(CreateDepartmentRequest req) {
		var principal = RequireRole.requireAny(Role.HOSPITAL_ADMIN);

		UserEntity admin = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));
		if (admin.getHospital() == null) {
			throw new BadRequestException("Hospital admin is not assigned to a hospital");
		}
		if (!admin.getHospital().getId().equals(req.hospitalId())) {
			throw new BadRequestException("Hospital admin can only manage their own hospital");
		}

		HospitalEntity hospital = hospitalRepository.findById(req.hospitalId())
				.orElseThrow(() -> new NotFoundException("Hospital not found: " + req.hospitalId()));
		if (!hospital.isActive()) {
			throw new BadRequestException("Hospital is inactive");
		}
		if (!hospital.isApproved()) {
			throw new BadRequestException("Hospital must be approved");
		}

		DepartmentEntity d = new DepartmentEntity();
		d.setHospital(hospital);
		d.setName(req.name());
		d.setDescription(req.description());
		d.setActive(true);
		return DepartmentMapper.toResponse(departmentRepository.save(d));
	}
}

