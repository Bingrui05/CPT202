package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.BookingResponse;
import com.cpt202.consultationbooking.dto.response.ExpertiseCategoryResponse;
import com.cpt202.consultationbooking.dto.response.LevelResponse;
import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import com.cpt202.consultationbooking.entity.Level;
import com.cpt202.consultationbooking.repository.ExpertiseCategoryRepository;
import com.cpt202.consultationbooking.repository.LevelRepository;
import com.cpt202.consultationbooking.service.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ExpertiseCategoryRepository expertiseCategoryRepository;
    private final LevelRepository levelRepository;
    private final BookingService bookingService;

    public AdminController(ExpertiseCategoryRepository expertiseCategoryRepository,
                          LevelRepository levelRepository,
                          BookingService bookingService) {
        this.expertiseCategoryRepository = expertiseCategoryRepository;
        this.levelRepository = levelRepository;
        this.bookingService = bookingService;
    }

    @GetMapping("/expertise-categories")
    public ApiResponse<List<ExpertiseCategoryResponse>> getAllExpertiseCategories() {
        List<ExpertiseCategoryResponse> categories = expertiseCategoryRepository.findAll()
                .stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("Expertise categories retrieved successfully", categories);
    }

    @GetMapping("/levels")
    public ApiResponse<List<LevelResponse>> getAllLevels() {
        List<LevelResponse> levels = levelRepository.findAll()
                .stream()
                .map(this::toLevelResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("Levels retrieved successfully", levels);
    }

    @GetMapping("/bookings")
    public ApiResponse<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ApiResponse.success("Bookings retrieved successfully", bookings);
    }

    private ExpertiseCategoryResponse toCategoryResponse(ExpertiseCategory category) {
        return new ExpertiseCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getStatus()
        );
    }

    private LevelResponse toLevelResponse(Level level) {
        return new LevelResponse(
                level.getLevelId(),
                level.getName()
        );
    }
}
