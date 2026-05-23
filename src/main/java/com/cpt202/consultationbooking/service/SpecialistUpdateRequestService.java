package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistUpdateRequest;
import com.cpt202.consultationbooking.dto.response.SpecialistUpdateRequestResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.SpecialistUpdateRequest;
import com.cpt202.consultationbooking.enums.*;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class SpecialistUpdateRequestService {

    private static final List<BookingStatus> BLOCKING_STATUSES = Arrays.asList(
            BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final SpecialistUpdateRequestRepository requestRepository;
    private final SpecialistRepository specialistRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final ExpertiseCategoryRepository categoryRepository;
    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;

    public SpecialistUpdateRequestService(SpecialistUpdateRequestRepository requestRepository,
            SpecialistRepository specialistRepository,
            AvailabilitySlotRepository slotRepository,
            ExpertiseCategoryRepository categoryRepository,
            BookingRepository bookingRepository,
            ObjectMapper objectMapper) {
        this.requestRepository = requestRepository;
        this.specialistRepository = specialistRepository;
        this.slotRepository = slotRepository;
        this.categoryRepository = categoryRepository;
        this.bookingRepository = bookingRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public SpecialistUpdateRequestResponse createRequest(CreateSpecialistUpdateRequest req) {
        Specialist specialist = specialistRepository.findById(req.getSpecialistId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        validateCreateRequest(req, specialist);

        SpecialistUpdateRequest request = SpecialistUpdateRequest.builder()
                .specialist(specialist)
                .requestType(req.getRequestType())
                .actionType(req.getActionType())
                .targetId(req.getTargetId())
                .currentValue(req.getCurrentValue())
                .requestedValue(req.getRequestedValue())
                .reason(req.getReason())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        SpecialistUpdateRequest saved = requestRepository.save(request);
        return SpecialistUpdateRequestResponse.fromEntity(saved);
    }

    private void validateCreateRequest(CreateSpecialistUpdateRequest req, Specialist specialist) {
        RequestType type = req.getRequestType();
        RequestAction action = req.getActionType();

        if (type == RequestType.CATEGORY && action == RequestAction.UPDATE) {
            try {
                JsonNode node = objectMapper.readTree(req.getRequestedValue());
                if (node.has("categoryId")) {
                    ExpertiseCategory cat = categoryRepository.findById(node.get("categoryId").asLong())
                            .orElseThrow(() -> new BusinessException("Requested category not found"));
                    if (!cat.isActive()) {
                        throw new BusinessException("Cannot request to change to an inactive category");
                    }
                }
            } catch (JsonProcessingException e) {
                throw new BusinessException("Invalid requestedValue JSON format for category change");
            }
        }

        if (type == RequestType.SLOT) {
            if (action == RequestAction.CREATE) {
                validateSlotCreateRequest(req);
            } else if (action == RequestAction.UPDATE) {
                validateSlotUpdateRequest(req, specialist);
            } else if (action == RequestAction.DEACTIVATE) {
                validateSlotDeactivateRequest(req, specialist);
            }
        }
    }

    private void validateSlotCreateRequest(CreateSpecialistUpdateRequest req) {
        try {
            JsonNode node = objectMapper.readTree(req.getRequestedValue());
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(node.get("dayOfWeek").asText().toUpperCase());
            LocalTime startTime = LocalTime.parse(node.get("startTime").asText());
            LocalTime endTime = LocalTime.parse(node.get("endTime").asText());

            if (!startTime.isBefore(endTime)) {
                throw new BusinessException("Start time must be before end time");
            }
            if (hasOverlappingSlot(req.getSpecialistId(), dayOfWeek, startTime, endTime, null)) {
                throw new BusinessException("This slot overlaps with an existing active slot on " + dayOfWeek);
            }
        } catch (JsonProcessingException e) {
            throw new BusinessException("Invalid requestedValue JSON format for slot creation: " + e.getMessage());
        }
    }

    private void validateSlotUpdateRequest(CreateSpecialistUpdateRequest req, Specialist specialist) {
        if (req.getTargetId() == null) {
            throw new BusinessException("targetId is required for slot update request");
        }
        AvailabilitySlot slot = slotRepository.findById(req.getTargetId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        if (!slot.getSpecialist().getSpecialistId().equals(specialist.getSpecialistId())) {
            throw new BusinessException("Slot does not belong to this specialist");
        }
        if (hasActiveBookings(slot.getSlotId())) {
            throw new BusinessException("Cannot update slot with active PENDING or CONFIRMED bookings");
        }

        try {
            JsonNode node = objectMapper.readTree(req.getRequestedValue());
            DayOfWeek dayOfWeek = node.has("dayOfWeek")
                    ? DayOfWeek.valueOf(node.get("dayOfWeek").asText().toUpperCase())
                    : slot.getDayOfWeek();
            LocalTime startTime = node.has("startTime") ? LocalTime.parse(node.get("startTime").asText()) : slot.getStartTime();
            LocalTime endTime = node.has("endTime") ? LocalTime.parse(node.get("endTime").asText()) : slot.getEndTime();

            if (!startTime.isBefore(endTime)) {
                throw new BusinessException("Start time must be before end time");
            }
            if (hasOverlappingSlot(req.getSpecialistId(), dayOfWeek, startTime, endTime, slot.getSlotId())) {
                throw new BusinessException("Updated slot would overlap with an existing active slot on " + dayOfWeek);
            }
        } catch (JsonProcessingException e) {
            throw new BusinessException("Invalid requestedValue JSON format for slot update: " + e.getMessage());
        }
    }

    private void validateSlotDeactivateRequest(CreateSpecialistUpdateRequest req, Specialist specialist) {
        if (req.getTargetId() == null) {
            throw new BusinessException("targetId is required for slot deactivate request");
        }
        AvailabilitySlot slot = slotRepository.findById(req.getTargetId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        if (!slot.getSpecialist().getSpecialistId().equals(specialist.getSpecialistId())) {
            throw new BusinessException("Slot does not belong to this specialist");
        }
        if (hasActiveBookings(slot.getSlotId())) {
            throw new BusinessException("Cannot deactivate slot with active PENDING or CONFIRMED bookings");
        }
    }

    private boolean hasActiveBookings(Long slotId) {
        return bookingRepository.existsBySlot_SlotIdAndStatusIn(slotId, BLOCKING_STATUSES);
    }

    private boolean hasOverlappingSlot(Long specialistId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Long excludeSlotId) {
        List<AvailabilitySlot> existingSlots = slotRepository.findBySpecialist_SpecialistIdAndDayOfWeek(specialistId, dayOfWeek);
        for (AvailabilitySlot existing : existingSlots) {
            if (excludeSlotId != null && existing.getSlotId().equals(excludeSlotId)) {
                continue;
            }
            if (existing.getStatus() == SlotStatus.UNAVAILABLE) {
                continue;
            }
            if (timesOverlap(existing.getStartTime(), existing.getEndTime(), startTime, endTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public List<SpecialistUpdateRequestResponse> getRequestsBySpecialist(Long specialistId) {
        if (!specialistRepository.existsById(specialistId)) {
            throw new ResourceNotFoundException("Specialist not found");
        }
        return requestRepository.findBySpecialist_SpecialistIdOrderByCreatedAtDesc(specialistId)
                .stream()
                .map(SpecialistUpdateRequestResponse::fromEntity)
                .toList();
    }

    public List<SpecialistUpdateRequestResponse> getPendingRequests() {
        return requestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.PENDING)
                .stream()
                .map(SpecialistUpdateRequestResponse::fromEntity)
                .toList();
    }

    public List<SpecialistUpdateRequestResponse> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(SpecialistUpdateRequestResponse::fromEntity)
                .toList();
    }

    @Transactional
    public SpecialistUpdateRequestResponse approveRequest(Long requestId, Long managerId) {
        SpecialistUpdateRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Only PENDING requests can be approved");
        }

        applyRequestChanges(request);

        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());
        request.setReviewedBy(managerId);
        SpecialistUpdateRequest saved = requestRepository.save(request);
        return SpecialistUpdateRequestResponse.fromEntity(saved);
    }

    private void applyRequestChanges(SpecialistUpdateRequest request) {
        RequestType type = request.getRequestType();
        RequestAction action = request.getActionType();

        if (type == RequestType.PROFILE) {
            applyProfileChange(request);
        } else if (type == RequestType.CATEGORY) {
            applyCategoryChange(request);
        } else if (type == RequestType.SLOT) {
            if (action == RequestAction.CREATE) {
                applySlotCreate(request);
            } else if (action == RequestAction.UPDATE) {
                applySlotUpdate(request);
            } else if (action == RequestAction.DEACTIVATE) {
                applySlotDeactivate(request);
            }
        }
    }

    private void applyProfileChange(SpecialistUpdateRequest request) {
        try {
            JsonNode node = objectMapper.readTree(request.getRequestedValue());
            Specialist specialist = request.getSpecialist();

            if (node.has("fee")) {
                specialist.setFee(new BigDecimal(node.get("fee").asText()));
            }
            if (node.has("information")) {
                specialist.setInformation(node.get("information").asText());
            }
            specialistRepository.save(specialist);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Invalid requestedValue JSON format for profile update");
        }
    }

    private void applyCategoryChange(SpecialistUpdateRequest request) {
        try {
            JsonNode node = objectMapper.readTree(request.getRequestedValue());
            if (node.has("categoryId")) {
                Long categoryId = node.get("categoryId").asLong();
                ExpertiseCategory category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                if (!category.isActive()) {
                    throw new BusinessException("Cannot change to inactive category");
                }
                Specialist specialist = request.getSpecialist();
                specialist.setCategory(category);
                specialistRepository.save(specialist);
            }
        } catch (JsonProcessingException e) {
            throw new BusinessException("Invalid requestedValue JSON format for category change");
        }
    }

    private void applySlotCreate(SpecialistUpdateRequest request) {
        try {
            JsonNode node = objectMapper.readTree(request.getRequestedValue());
            Specialist specialist = request.getSpecialist();
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(node.get("dayOfWeek").asText().toUpperCase());
            LocalTime startTime = LocalTime.parse(node.get("startTime").asText());
            LocalTime endTime = LocalTime.parse(node.get("endTime").asText());
            SlotStatus status = SlotStatus.AVAILABLE;

            AvailabilitySlot slot = AvailabilitySlot.builder()
                    .specialist(specialist)
                    .dayOfWeek(dayOfWeek)
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(status)
                    .build();

            slotRepository.save(slot);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Invalid requestedValue JSON format for slot creation");
        }
    }

    private void applySlotUpdate(SpecialistUpdateRequest request) {
        try {
            JsonNode node = objectMapper.readTree(request.getRequestedValue());
            AvailabilitySlot slot = slotRepository.findById(request.getTargetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

            if (node.has("dayOfWeek")) {
                slot.setDayOfWeek(DayOfWeek.valueOf(node.get("dayOfWeek").asText().toUpperCase()));
            }
            if (node.has("startTime")) {
                slot.setStartTime(LocalTime.parse(node.get("startTime").asText()));
            }
            if (node.has("endTime")) {
                slot.setEndTime(LocalTime.parse(node.get("endTime").asText()));
            }
            if (node.has("status")) {
                slot.setStatus(SlotStatus.valueOf(node.get("status").asText().toUpperCase()));
            }

            slotRepository.save(slot);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Invalid requestedValue JSON format for slot update");
        }
    }

    private void applySlotDeactivate(SpecialistUpdateRequest request) {
        AvailabilitySlot slot = slotRepository.findById(request.getTargetId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        slot.setStatus(SlotStatus.UNAVAILABLE);
        slotRepository.save(slot);
    }

    @Transactional
    public SpecialistUpdateRequestResponse rejectRequest(Long requestId, String managerComment, Long managerId) {
        SpecialistUpdateRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Only PENDING requests can be rejected");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setManagerComment(managerComment);
        request.setReviewedAt(LocalDateTime.now());
        request.setReviewedBy(managerId);
        SpecialistUpdateRequest saved = requestRepository.save(request);
        return SpecialistUpdateRequestResponse.fromEntity(saved);
    }
}
