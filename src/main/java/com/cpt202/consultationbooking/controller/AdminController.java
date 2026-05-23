package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.CreateExpertiseCategoryRequest;
import com.cpt202.consultationbooking.dto.request.UpdateExpertiseCategoryRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.BookingResponse;
import com.cpt202.consultationbooking.dto.response.ExpertiseCategoryResponse;
import com.cpt202.consultationbooking.dto.response.LevelResponse;
import com.cpt202.consultationbooking.entity.Level;
import com.cpt202.consultationbooking.repository.LevelRepository;
import com.cpt202.consultationbooking.service.AdminCategoryService;
import com.cpt202.consultationbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminCategoryService adminCategoryService;
    private final LevelRepository levelRepository;
    private final BookingService bookingService;

    public AdminController(AdminCategoryService adminCategoryService,
                          LevelRepository levelRepository,
                          BookingService bookingService) {
        this.adminCategoryService = adminCategoryService;
        this.levelRepository = levelRepository;
        this.bookingService = bookingService;
    }

    @GetMapping("/expertise-categories")
    public ResponseEntity<ApiResponse<List<ExpertiseCategoryResponse>>> getAllExpertiseCategories() {
        List<ExpertiseCategoryResponse> categories = adminCategoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Expertise categories retrieved successfully", categories));
    }

    @GetMapping("/expertise-categories/{categoryId}")
    public ResponseEntity<ApiResponse<ExpertiseCategoryResponse>> getExpertiseCategoryById(@PathVariable Long categoryId) {
        ExpertiseCategoryResponse category = adminCategoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Expertise category retrieved successfully", category));
    }

    @PostMapping("/expertise-categories")
    public ResponseEntity<ApiResponse<ExpertiseCategoryResponse>> createExpertiseCategory(
            @Valid @RequestBody CreateExpertiseCategoryRequest request) {
        ExpertiseCategoryResponse created = adminCategoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Expertise category created successfully", created));
    }

    @PutMapping("/expertise-categories/{categoryId}")
    public ResponseEntity<ApiResponse<ExpertiseCategoryResponse>> updateExpertiseCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody UpdateExpertiseCategoryRequest request) {
        ExpertiseCategoryResponse updated = adminCategoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.success("Expertise category updated successfully", updated));
    }

    @DeleteMapping("/expertise-categories/{categoryId}")
    public ResponseEntity<ApiResponse<ExpertiseCategoryResponse>> deleteExpertiseCategory(@PathVariable Long categoryId) {
        ExpertiseCategoryResponse deactivated = adminCategoryService.deactivateCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Expertise category deactivated successfully", deactivated));
    }

    @GetMapping("/levels")
    public ResponseEntity<ApiResponse<List<LevelResponse>>> getAllLevels() {
        List<LevelResponse> levels = levelRepository.findAll()
                .stream()
                .map(level -> new LevelResponse(level.getLevelId(), level.getName()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Levels retrieved successfully", levels));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }
}
