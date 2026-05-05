package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistRequest;
import com.cpt202.consultationbooking.dto.response.SpecialistResponse;
import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import com.cpt202.consultationbooking.entity.Level;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.enums.SpecialistStatus;
import com.cpt202.consultationbooking.enums.UserRole;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.ExpertiseCategoryRepository;
import com.cpt202.consultationbooking.repository.LevelRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import com.cpt202.consultationbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final UserRepository userRepository;
    private final ExpertiseCategoryRepository categoryRepository;
    private final LevelRepository levelRepository;

    public SpecialistService(SpecialistRepository specialistRepository,
                             UserRepository userRepository,
                             ExpertiseCategoryRepository categoryRepository,
                             LevelRepository levelRepository) {
        this.specialistRepository = specialistRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.levelRepository = levelRepository;
    }

    @Transactional
    public SpecialistResponse createSpecialist(CreateSpecialistRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != UserRole.SPECIALIST) {
            throw new BusinessException("User must have SPECIALIST role");
        }

        ExpertiseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Level level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        Specialist specialist = Specialist.builder()
                .user(user)
                .category(category)
                .level(level)
                .status(request.getStatus())
                .fee(request.getFee())
                .information(request.getInformation())
                .build();

        Specialist saved = specialistRepository.save(specialist);
        return toResponse(saved);
    }

    public List<SpecialistResponse> getAllSpecialists() {
        return specialistRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SpecialistResponse> searchSpecialists(Long categoryId, Long levelId, String status) {
        List<Specialist> specialists;

        if (categoryId != null && levelId != null && status != null) {
            specialists = specialistRepository.findByCategory_CategoryIdAndLevel_LevelIdAndStatus(
                    categoryId, levelId, SpecialistStatus.valueOf(status.toUpperCase()));
        } else if (categoryId != null && levelId != null) {
            specialists = specialistRepository.findByCategory_CategoryIdAndLevel_LevelId(categoryId, levelId);
        } else if (categoryId != null && status != null) {
            specialists = specialistRepository.findByCategory_CategoryIdAndStatus(
                    categoryId, SpecialistStatus.valueOf(status.toUpperCase()));
        } else if (levelId != null && status != null) {
            specialists = specialistRepository.findByLevel_LevelIdAndStatus(
                    levelId, SpecialistStatus.valueOf(status.toUpperCase()));
        } else if (categoryId != null) {
            specialists = specialistRepository.findByCategory_CategoryId(categoryId);
        } else if (levelId != null) {
            specialists = specialistRepository.findByLevel_LevelId(levelId);
        } else if (status != null) {
            specialists = specialistRepository.findByStatus(SpecialistStatus.valueOf(status.toUpperCase()));
        } else {
            specialists = specialistRepository.findAll();
        }

        return specialists.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SpecialistResponse getSpecialistById(Long id) {
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));
        return toResponse(specialist);
    }

    private SpecialistResponse toResponse(Specialist specialist) {
        return SpecialistResponse.builder()
                .specialistId(specialist.getSpecialistId())
                .userId(specialist.getUser().getUserId())
                .username(specialist.getUser().getUsername())
                .email(specialist.getUser().getEmail())
                .categoryId(specialist.getCategory().getCategoryId())
                .categoryName(specialist.getCategory().getName())
                .levelId(specialist.getLevel().getLevelId())
                .levelName(specialist.getLevel().getName())
                .status(specialist.getStatus())
                .fee(specialist.getFee())
                .information(specialist.getInformation())
                .build();
    }
}
