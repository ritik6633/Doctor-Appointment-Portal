package com.doctorportal.hospital;

import com.doctorportal.auth.RequireRole;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.hospital.dto.CreateHospitalRequest;
import com.doctorportal.hospital.dto.HospitalResponse;
import com.doctorportal.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {
	private final HospitalRepository hospitalRepository;

	@Transactional(readOnly = true)
	public List<HospitalResponse> listHospitals(boolean includeUnapproved) {
		return hospitalRepository.findAll().stream()
				.filter(h -> h.isActive())
				.filter(h -> includeUnapproved || h.isApproved())
				.map(HospitalMapper::toResponse)
				.toList();
	}

	@Transactional
	public HospitalResponse createHospital(CreateHospitalRequest req) {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		HospitalEntity h = new HospitalEntity();
		h.setName(req.name());
		h.setCity(req.city());
		h.setAddress(req.address());
		h.setContactEmail(req.contactEmail());
		h.setContactPhone(req.contactPhone());
		h.setApproved(false);
		h.setActive(true);
		return HospitalMapper.toResponse(hospitalRepository.save(h));
	}

	@Transactional
	public HospitalResponse approveHospital(Long id) {
		RequireRole.requireAny(Role.DEVELOPER_ADMIN);

		HospitalEntity h = hospitalRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Hospital not found: " + id));
		h.setApproved(true);
		return HospitalMapper.toResponse(h);
	}
}

