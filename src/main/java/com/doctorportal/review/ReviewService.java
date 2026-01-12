package com.doctorportal.review;

import com.doctorportal.auth.AuthPrincipal;
import com.doctorportal.auth.RequireRole;
import com.doctorportal.appointment.AppointmentRepository;
import com.doctorportal.appointment.AppointmentStatus;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.NotFoundException;
import com.doctorportal.doctor.DoctorEntity;
import com.doctorportal.doctor.DoctorRepository;
import com.doctorportal.review.dto.CreateReviewRequest;
import com.doctorportal.review.dto.ReviewResponse;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;
	private final AppointmentRepository appointmentRepository;

	@Transactional
	public ReviewResponse create(CreateReviewRequest req) {
		AuthPrincipal principal = RequireRole.requireAny(Role.PATIENT);

		DoctorEntity doctor = doctorRepository.findById(req.doctorId())
				.orElseThrow(() -> new NotFoundException("Doctor not found: " + req.doctorId()));

		UserEntity patient = userRepository.findById(principal.userId())
				.orElseThrow(() -> new NotFoundException("User not found: " + principal.userId()));

		boolean completed = appointmentRepository.existsByDoctorIdAndPatientIdAndStatus(doctor.getId(), patient.getId(), AppointmentStatus.COMPLETED);
		if (!completed) {
			throw new BadRequestException("You can review a doctor only after a COMPLETED appointment");
		}

		boolean alreadyReviewed = reviewRepository.existsByDoctorIdAndPatientId(doctor.getId(), patient.getId());
		if (alreadyReviewed) {
			throw new BadRequestException("You have already reviewed this doctor");
		}

		ReviewEntity r = new ReviewEntity();
		r.setDoctor(doctor);
		r.setPatient(patient);
		r.setRating(req.rating());
		r.setComment(req.comment());

		return ReviewMapper.toResponse(reviewRepository.save(r));
	}

	@Transactional(readOnly = true)
	public List<ReviewResponse> listByDoctor(Long doctorId) {
		return reviewRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId).stream()
				.map(ReviewMapper::toResponse)
				.toList();
	}
}

