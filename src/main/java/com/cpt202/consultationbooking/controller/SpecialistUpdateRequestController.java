package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistUpdateRequest;
import com.cpt202.consultationbooking.dto.request.ProcessRequestRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.SpecialistUpdateRequestResponse;
import com.cpt202.consultationbooking.service.SpecialistUpdateRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialist-requests")
public class SpecialistUpdateRequestController {

    private final SpecialistUpdateRequestService requestService;

    public SpecialistUpdateRequestController(SpecialistUpdateRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> createRequest(
            @Valid @RequestBody CreateSpecialistUpdateRequest request) {
        SpecialistUpdateRequestResponse response = requestService.createRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Request submitted successfully", response));
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<ApiResponse<List<SpecialistUpdateRequestResponse>>> getRequestsBySpecialist(
            @PathVariable Long specialistId) {
        List<SpecialistUpdateRequestResponse> requests = requestService.getRequestsBySpecialist(specialistId);
        return ResponseEntity.ok(ApiResponse.success("Requests retrieved successfully", requests));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<SpecialistUpdateRequestResponse>>> getPendingRequests() {
        List<SpecialistUpdateRequestResponse> requests = requestService.getPendingRequests();
        return ResponseEntity.ok(ApiResponse.success("Pending requests retrieved successfully", requests));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecialistUpdateRequestResponse>>> getAllRequests() {
        List<SpecialistUpdateRequestResponse> requests = requestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success("All requests retrieved successfully", requests));
    }

    @PutMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> approveRequest(
            @PathVariable Long requestId,
            @RequestParam Long managerId) {
        SpecialistUpdateRequestResponse response = requestService.approveRequest(requestId, managerId);
        return ResponseEntity.ok(ApiResponse.success("Request approved successfully", response));
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) Long managerId,
            @RequestBody(required = false) ProcessRequestRequest requestBody) {
        String comment = (requestBody != null) ? requestBody.getManagerComment() : null;
        Long manager = (managerId != null) ? managerId : null;
        SpecialistUpdateRequestResponse response = requestService.rejectRequest(requestId, comment, manager);
        return ResponseEntity.ok(ApiResponse.success("Request rejected successfully", response));
    }
}
