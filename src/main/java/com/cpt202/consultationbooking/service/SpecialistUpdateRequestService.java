package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistUpdateRequest;
import com.cpt202.consultationbooking.dto.request.ProcessSpecialistUpdateRequest;
import com.cpt202.consultationbooking.dto.response.SpecialistUpdateRequestResponse;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.SpecialistUpdateRequest;
import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestStatus;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import com.cpt202.consultationbooking.repository.SpecialistUpdateRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialistUpdateRequestService {

    private final SpecialistUpdateRequestRepository requestRepository;
    private final SpecialistRepository specialistRepository;

    public SpecialistUpdateRequestService(SpecialistUpdateRequestRepository requestRepository,
                                          SpecialistRepository specialistRepository) {
        this.requestRepository = requestRepository;
        this.specialistRepository = specialistRepository;
    }

    @Transactional
    public SpecialistUpdateRequestResponse createRequest(CreateSpecialistUpdateRequest request) {
        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        SpecialistUpdateRequest updateRequest = SpecialistUpdateRequest.builder()
                .specialist(specialist)
                .requestType(request.getRequestType())
                .fieldName(request.getFieldName())
                .oldValue(request.getOldValue())
                .newValue(request.getNewValue())
                .reason(request.getReason())
                .status(SpecialistUpdateRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        SpecialistUpdateRequest saved = requestRepository.save(updateRequest);
        return toResponse(saved);
    }

    public List<SpecialistUpdateRequestResponse> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SpecialistUpdateRequestResponse> getPendingRequests() {
        return requestRepository.findByStatusOrderByCreatedAtDesc(SpecialistUpdateRequestStatus.PENDING).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SpecialistUpdateRequestResponse> getRequestsBySpecialist(Long specialistId) {
        if (!specialistRepository.existsById(specialistId)) {
            throw new ResourceNotFoundException("Specialist not found");
        }

        return requestRepository.findBySpecialist_SpecialistIdOrderByCreatedAtDesc(specialistId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SpecialistUpdateRequestResponse getRequestById(Long requestId) {
        SpecialistUpdateRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist update request not found"));
        return toResponse(request);
    }

    @Transactional
    public SpecialistUpdateRequestResponse markAsReviewed(Long requestId, ProcessSpecialistUpdateRequest processRequest) {
        SpecialistUpdateRequest request = getPendingRequest(requestId);
        request.setStatus(SpecialistUpdateRequestStatus.REVIEWED);
        request.setManagerComment(processRequest != null ? processRequest.getManagerComment() : null);
        request.setReviewedAt(LocalDateTime.now());

        SpecialistUpdateRequest saved = requestRepository.save(request);
        return toResponse(saved);
    }

    @Transactional
    public SpecialistUpdateRequestResponse rejectRequest(Long requestId, ProcessSpecialistUpdateRequest processRequest) {
        SpecialistUpdateRequest request = getPendingRequest(requestId);
        request.setStatus(SpecialistUpdateRequestStatus.REJECTED);
        request.setManagerComment(processRequest != null ? processRequest.getManagerComment() : null);
        request.setReviewedAt(LocalDateTime.now());

        SpecialistUpdateRequest saved = requestRepository.save(request);
        return toResponse(saved);
    }

    private SpecialistUpdateRequest getPendingRequest(Long requestId) {
        SpecialistUpdateRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist update request not found"));

        if (request.getStatus() != SpecialistUpdateRequestStatus.PENDING) {
            throw new BusinessException("Only pending requests can be processed");
        }

        return request;
    }

    private SpecialistUpdateRequestResponse toResponse(SpecialistUpdateRequest request) {
        return SpecialistUpdateRequestResponse.builder()
                .requestId(request.getRequestId())
                .specialistId(request.getSpecialist().getSpecialistId())
                .specialistName(request.getSpecialist().getUser().getUsername())
                .requestType(request.getRequestType())
                .fieldName(request.getFieldName())
                .oldValue(request.getOldValue())
                .newValue(request.getNewValue())
                .reason(request.getReason())
                .status(request.getStatus())
                .managerComment(request.getManagerComment())
                .createdAt(request.getCreatedAt())
                .reviewedAt(request.getReviewedAt())
                .build();
    }
}
