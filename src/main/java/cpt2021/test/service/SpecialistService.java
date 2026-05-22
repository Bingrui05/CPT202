package cpt2021.test.service;

import cpt2021.test.dto.SpecialistRequest;
import cpt2021.test.dto.SpecialistResponse;
import cpt2021.test.entity.ExpertiseCategory;
import cpt2021.test.entity.Level;
import cpt2021.test.entity.Specialist;
import cpt2021.test.exception.ResourceNotFoundException;
import cpt2021.test.repository.ExpertiseCategoryRepository;
import cpt2021.test.repository.LevelRepository;
import cpt2021.test.repository.SpecialistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialistService {

	private final SpecialistRepository specialistRepository;
	private final ExpertiseCategoryRepository expertiseCategoryRepository;
	private final LevelRepository levelRepository;

	public SpecialistService(
			SpecialistRepository specialistRepository,
			ExpertiseCategoryRepository expertiseCategoryRepository,
			LevelRepository levelRepository) {
		this.specialistRepository = specialistRepository;
		this.expertiseCategoryRepository = expertiseCategoryRepository;
		this.levelRepository = levelRepository;
	}

	@Transactional(readOnly = true)
	public List<SpecialistResponse> listAll() {
		return specialistRepository.findAllFetched().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public SpecialistResponse getById(Long id) {
		Specialist s = specialistRepository
				.findByIdFetched(id)
				.orElseThrow(() -> new ResourceNotFoundException("Specialist not found: " + id));
		return toResponse(s);
	}

	@Transactional
	public SpecialistResponse create(SpecialistRequest request) {
		ExpertiseCategory category = expertiseCategoryRepository
				.findById(request.expertiseCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"ExpertiseCategory not found: " + request.expertiseCategoryId()));
		Level level = levelRepository
				.findById(request.levelId())
				.orElseThrow(() -> new ResourceNotFoundException("Level not found: " + request.levelId()));

		Specialist specialist = new Specialist(
				request.displayName(),
				emptyToNull(request.email()),
				emptyToNull(request.phone()),
				emptyToNull(request.bio()),
				category,
				level);
		Specialist saved = specialistRepository.save(specialist);
		return specialistRepository
				.findByIdFetched(saved.getId())
				.map(this::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Specialist not found after create"));
	}

	@Transactional
	public SpecialistResponse update(Long id, SpecialistRequest request) {
		Specialist specialist = specialistRepository
				.findByIdFetched(id)
				.orElseThrow(() -> new ResourceNotFoundException("Specialist not found: " + id));

		ExpertiseCategory category = expertiseCategoryRepository
				.findById(request.expertiseCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"ExpertiseCategory not found: " + request.expertiseCategoryId()));
		Level level = levelRepository
				.findById(request.levelId())
				.orElseThrow(() -> new ResourceNotFoundException("Level not found: " + request.levelId()));

		specialist.setDisplayName(request.displayName());
		specialist.setEmail(emptyToNull(request.email()));
		specialist.setPhone(emptyToNull(request.phone()));
		specialist.setBio(emptyToNull(request.bio()));
		specialist.setExpertiseCategory(category);
		specialist.setLevel(level);

		specialistRepository.save(specialist);
		return specialistRepository
				.findByIdFetched(id)
				.map(this::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Specialist not found: " + id));
	}

	@Transactional
	public void delete(Long id) {
		if (!specialistRepository.existsById(id)) {
			throw new ResourceNotFoundException("Specialist not found: " + id);
		}
		specialistRepository.deleteById(id);
	}

	private SpecialistResponse toResponse(Specialist s) {
		return new SpecialistResponse(
				s.getId(),
				s.getDisplayName(),
				s.getEmail(),
				s.getPhone(),
				s.getBio(),
				s.getExpertiseCategory().getId(),
				s.getExpertiseCategory().getName(),
				s.getLevel().getId(),
				s.getLevel().getName(),
				s.getCreatedAt(),
				s.getUpdatedAt());
	}

	private static String emptyToNull(String v) {
		if (v == null || v.isBlank()) {
			return null;
		}
		return v.trim();
	}
}
