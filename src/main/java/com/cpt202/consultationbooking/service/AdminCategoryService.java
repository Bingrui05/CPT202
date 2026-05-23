package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateExpertiseCategoryRequest;
import com.cpt202.consultationbooking.dto.request.UpdateExpertiseCategoryRequest;
import com.cpt202.consultationbooking.dto.response.ExpertiseCategoryResponse;
import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.ExpertiseCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCategoryService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_INACTIVE = "INACTIVE";

    private final ExpertiseCategoryRepository categoryRepository;

    public AdminCategoryService(ExpertiseCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ExpertiseCategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpertiseCategoryResponse getCategoryById(Long categoryId) {
        ExpertiseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Expertise category not found with id: " + categoryId));
        return toResponse(category);
    }

    @Transactional
    public ExpertiseCategoryResponse createCategory(CreateExpertiseCategoryRequest request) {
        String normalizedName = request.getName().trim();

        if (categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BusinessException("Expertise category with name '" + normalizedName + "' already exists");
        }

        String status = request.getStatus();
        if (status == null || status.trim().isEmpty()) {
            status = STATUS_ACTIVE;
        } else {
            status = status.trim().toUpperCase();
        }

        ExpertiseCategory category = ExpertiseCategory.builder()
                .name(normalizedName)
                .status(status)
                .build();

        ExpertiseCategory saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    @Transactional
    public ExpertiseCategoryResponse updateCategory(Long categoryId, UpdateExpertiseCategoryRequest request) {
        ExpertiseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Expertise category not found with id: " + categoryId));

        String newName = request.getName().trim();
        String newStatus = request.getStatus();

        if (newStatus != null && !newStatus.trim().isEmpty()) {
            newStatus = newStatus.trim().toUpperCase();
            if (!STATUS_ACTIVE.equals(newStatus) && !STATUS_INACTIVE.equals(newStatus)) {
                throw new BusinessException("Status must be ACTIVE or INACTIVE");
            }
        }

        if (!category.getName().equalsIgnoreCase(newName)) {
            if (categoryRepository.existsByNameIgnoreCase(newName)) {
                throw new BusinessException("Expertise category with name '" + newName + "' already exists");
            }
            category.setName(newName);
        }

        if (newStatus != null && !newStatus.trim().isEmpty()) {
            category.setStatus(newStatus);
        }

        ExpertiseCategory saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    @Transactional
    public ExpertiseCategoryResponse deactivateCategory(Long categoryId) {
        ExpertiseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Expertise category not found with id: " + categoryId));

        category.setStatus(STATUS_INACTIVE);
        ExpertiseCategory saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    private ExpertiseCategoryResponse toResponse(ExpertiseCategory category) {
        return new ExpertiseCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getStatus()
        );
    }
}
