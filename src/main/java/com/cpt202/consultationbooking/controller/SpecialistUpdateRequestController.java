package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistUpdateRequest;
import com.cpt202.consultationbooking.dto.request.ProcessSpecialistUpdateRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.SpecialistUpdateRequestResponse;
import com.cpt202.consultationbooking.service.SpecialistUpdateRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialist-update-requests")
public class SpecialistUpdateRequestController {

    private final SpecialistUpdateRequestService updateRequestService;

    public SpecialistUpdateRequestController(SpecialistUpdateRequestService updateRequestService) {
        this.updateRequestService = updateRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> createRequest(
            @Valid @RequestBody CreateSpecialistUpdateRequest request) {
        SpecialistUpdateRequestResponse response = updateRequestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Specialist update request submitted successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecialistUpdateRequestResponse>>> getAllRequests() {
        List<SpecialistUpdateRequestResponse> requests = updateRequestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success("Specialist update requests retrieved successfully", requests));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<SpecialistUpdateRequestResponse>>> getPendingRequests() {
        List<SpecialistUpdateRequestResponse> requests = updateRequestService.getPendingRequests();
        return ResponseEntity.ok(ApiResponse.success("Pending specialist update requests retrieved successfully", requests));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> getRequestById(@PathVariable Long requestId) {
        SpecialistUpdateRequestResponse response = updateRequestService.getRequestById(requestId);
        return ResponseEntity.ok(ApiResponse.success("Specialist update request retrieved successfully", response));
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<ApiResponse<List<SpecialistUpdateRequestResponse>>> getRequestsBySpecialist(
            @PathVariable Long specialistId) {
        List<SpecialistUpdateRequestResponse> requests = updateRequestService.getRequestsBySpecialist(specialistId);
        return ResponseEntity.ok(ApiResponse.success("Specialist update requests retrieved successfully", requests));
    }

    @PutMapping("/{requestId}/review")
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> markAsReviewed(
            @PathVariable Long requestId,
            @Valid @RequestBody(required = false) ProcessSpecialistUpdateRequest request) {
        SpecialistUpdateRequestResponse response = updateRequestService.markAsReviewed(requestId, request);
        return ResponseEntity.ok(ApiResponse.success("Specialist update request marked as reviewed", response));
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<SpecialistUpdateRequestResponse>> rejectRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody(required = false) ProcessSpecialistUpdateRequest request) {
        SpecialistUpdateRequestResponse response = updateRequestService.rejectRequest(requestId, request);
        return ResponseEntity.ok(ApiResponse.success("Specialist update request rejected", response));
    }
}
