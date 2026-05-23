package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.CreateSlotRequest;
import com.cpt202.consultationbooking.dto.request.UpdateSlotRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.AvailableSlotResponse;
import com.cpt202.consultationbooking.dto.response.SlotResponse;
import com.cpt202.consultationbooking.service.AvailabilitySlotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class AvailabilitySlotController {

    private final AvailabilitySlotService slotService;

    public AvailabilitySlotController(AvailabilitySlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SlotResponse>> createSlot(@Valid @RequestBody CreateSlotRequest request) {
        SlotResponse response = slotService.createSlot(request);
        return ResponseEntity.ok(ApiResponse.success("Slot created successfully", response));
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<ApiResponse<SlotResponse>> getSlotById(@PathVariable Long slotId) {
        SlotResponse response = slotService.getSlotById(slotId);
        return ResponseEntity.ok(ApiResponse.success("Slot retrieved successfully", response));
    }

    @PutMapping("/{slotId}")
    public ResponseEntity<ApiResponse<SlotResponse>> updateSlot(
            @PathVariable Long slotId,
            @RequestBody UpdateSlotRequest request) {
        SlotResponse response = slotService.updateSlot(slotId, request);
        return ResponseEntity.ok(ApiResponse.success("Slot updated successfully", response));
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<ApiResponse<Void>> deleteSlot(@PathVariable Long slotId) {
        slotService.deleteSlot(slotId);
        return ResponseEntity.ok(ApiResponse.success("Slot deactivated successfully", null));
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<ApiResponse<List<SlotResponse>>> getSlotsBySpecialist(
            @PathVariable Long specialistId) {
        List<SlotResponse> slots = slotService.getSlotsBySpecialist(specialistId);
        return ResponseEntity.ok(ApiResponse.success("Slots retrieved successfully", slots));
    }

    @GetMapping("/specialist/{specialistId}/available")
    public ResponseEntity<ApiResponse<List<AvailableSlotResponse>>> getAvailableSlotsBySpecialist(
            @PathVariable Long specialistId) {
        List<AvailableSlotResponse> slots = slotService.getAvailableSlotsBySpecialist(specialistId);
        return ResponseEntity.ok(ApiResponse.success("Available slots retrieved successfully", slots));
    }
}
