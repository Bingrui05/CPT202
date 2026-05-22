package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistRequest;
import com.cpt202.consultationbooking.dto.request.UpdateSpecialistRequest;
import com.cpt202.consultationbooking.dto.response.SpecialistResponse;
import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import com.cpt202.consultationbooking.entity.Level;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.enums.BookingStatus;
import com.cpt202.consultationbooking.enums.SpecialistStatus;
import com.cpt202.consultationbooking.enums.UserRole;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.BookingRepository;
import com.cpt202.consultationbooking.repository.ExpertiseCategoryRepository;
import com.cpt202.consultationbooking.repository.LevelRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import com.cpt202.consultationbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialistService {

    private static final List<BookingStatus> ACTIVE_BOOKING_STATUSES = Arrays.asList(
            BookingStatus.PENDING, 
            BookingStatus.CONFIRMED
    );

    private final SpecialistRepository specialistRepository;
    private final UserRepository userRepository;
    private final ExpertiseCategoryRepository categoryRepository;
    private final LevelRepository levelRepository;
    private final BookingRepository bookingRepository;

    public SpecialistService(SpecialistRepository specialistRepository,
                           UserRepository userRepository,
                           ExpertiseCategoryRepository categoryRepository,
                           LevelRepository levelRepository,
                           BookingRepository bookingRepository) {
        this.specialistRepository = specialistRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.levelRepository = levelRepository;
        this.bookingRepository = bookingRepository;
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

        // Check if category is active
        if (!category.isActive()) {
            throw new BusinessException("Cannot assign inactive category to specialist");
        }

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

    public List<SpecialistResponse> searchSpecialists(String keyword, Long categoryId, Long levelId, String status, boolean onlyAvailable) {
        List<Specialist> specialists;

        if (keyword != null && !keyword.trim().isEmpty()) {
            SpecialistStatus specialistStatus = null;
            if (status != null && !status.trim().isEmpty()) {
                specialistStatus = SpecialistStatus.valueOf(status.toUpperCase());
            }
            specialists = specialistRepository.searchByKeywordWithFilters(
                    keyword.trim(), categoryId, levelId, specialistStatus, onlyAvailable);
        } else {
            specialists = performFilterSearch(categoryId, levelId, status, onlyAvailable);
        }

        return specialists.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private List<Specialist> performFilterSearch(Long categoryId, Long levelId, String status, boolean onlyAvailable) {
        SpecialistStatus specialistStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            specialistStatus = SpecialistStatus.valueOf(status.toUpperCase());
        }
        return specialistRepository.searchByFiltersOnly(categoryId, levelId, specialistStatus, onlyAvailable);
    }

    public SpecialistResponse getSpecialistById(Long id) {
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));
        return toResponse(specialist);
    }

    @Transactional
    public SpecialistResponse updateSpecialist(Long specialistId, UpdateSpecialistRequest request) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        if (request.getCategoryId() != null) {
            ExpertiseCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            // Check if category is active
            if (!category.isActive()) {
                throw new BusinessException("Cannot assign inactive category to specialist");
            }
            specialist.setCategory(category);
        }

        if (request.getLevelId() != null) {
            Level level = levelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Level not found"));
            specialist.setLevel(level);
        }

        if (request.getFee() != null) {
            specialist.setFee(request.getFee());
        }

        if (request.getStatus() != null) {
            specialist.setStatus(request.getStatus());
        }

        if (request.getInformation() != null) {
            specialist.setInformation(request.getInformation());
        }

        Specialist updated = specialistRepository.save(specialist);
        return toResponse(updated);
    }

    @Transactional
    public void deactivateSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        // Check if specialist has active bookings
        boolean hasActiveBookings = bookingRepository.existsBySpecialist_SpecialistIdAndStatusIn(
                specialistId, ACTIVE_BOOKING_STATUSES);

        if (hasActiveBookings) {
            throw new BusinessException("Specialist has active bookings and cannot be deactivated");
        }

        specialist.setStatus(SpecialistStatus.INACTIVE);
        specialistRepository.save(specialist);
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
