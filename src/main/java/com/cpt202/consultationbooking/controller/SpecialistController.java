package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.SpecialistResponse;
import com.cpt202.consultationbooking.service.SpecialistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialists")
public class SpecialistController {

    private final SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpecialistResponse>> createSpecialist(
            @Valid @RequestBody CreateSpecialistRequest request) {
        SpecialistResponse response = specialistService.createSpecialist(request);
        return ResponseEntity.ok(ApiResponse.success("Specialist created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecialistResponse>>> getAllSpecialists() {
        List<SpecialistResponse> specialists = specialistService.getAllSpecialists();
        return ResponseEntity.ok(ApiResponse.success("Specialists retrieved successfully", specialists));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecialistResponse>> getSpecialistById(@PathVariable Long id) {
        SpecialistResponse response = specialistService.getSpecialistById(id);
        return ResponseEntity.ok(ApiResponse.success("Specialist retrieved successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SpecialistResponse>>> searchSpecialists(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long levelId,
            @RequestParam(required = false) String status) {
        List<SpecialistResponse> specialists = specialistService.searchSpecialists(categoryId, levelId, status);
        return ResponseEntity.ok(ApiResponse.success("Search completed", specialists));
    }
}
