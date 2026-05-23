package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.entity.SpecialistUpdateRequest;
import com.cpt202.consultationbooking.enums.RequestAction;
import com.cpt202.consultationbooking.enums.RequestStatus;
import com.cpt202.consultationbooking.enums.RequestType;
import java.time.LocalDateTime;

public class SpecialistUpdateRequestResponse {

    private Long requestId;
    private Long specialistId;
    private String specialistName;
    private String specialistEmail;
    private RequestType requestType;
    private RequestAction actionType;
    private Long targetId;
    private String currentValue;
    private String requestedValue;
    private String reason;
    private RequestStatus status;
    private String managerComment;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private Long reviewedBy;

    public SpecialistUpdateRequestResponse() {}

    public SpecialistUpdateRequestResponse(Long requestId, Long specialistId, String specialistName,
            String specialistEmail, RequestType requestType, RequestAction actionType, Long targetId,
            String currentValue, String requestedValue, String reason, RequestStatus status,
            String managerComment, LocalDateTime createdAt, LocalDateTime reviewedAt, Long reviewedBy) {
        this.requestId = requestId;
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.specialistEmail = specialistEmail;
        this.requestType = requestType;
        this.actionType = actionType;
        this.targetId = targetId;
        this.currentValue = currentValue;
        this.requestedValue = requestedValue;
        this.reason = reason;
        this.status = status;
        this.managerComment = managerComment;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
        this.reviewedBy = reviewedBy;
    }

    public static SpecialistUpdateRequestResponse fromEntity(SpecialistUpdateRequest entity) {
        return new SpecialistUpdateRequestResponseBuilder()
                .requestId(entity.getRequestId())
                .specialistId(entity.getSpecialist().getSpecialistId())
                .specialistName(entity.getSpecialist().getUser().getUsername())
                .specialistEmail(entity.getSpecialist().getUser().getEmail())
                .requestType(entity.getRequestType())
                .actionType(entity.getActionType())
                .targetId(entity.getTargetId())
                .currentValue(entity.getCurrentValue())
                .requestedValue(entity.getRequestedValue())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .managerComment(entity.getManagerComment())
                .createdAt(entity.getCreatedAt())
                .reviewedAt(entity.getReviewedAt())
                .reviewedBy(entity.getReviewedBy())
                .build();
    }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public String getSpecialistName() { return specialistName; }
    public void setSpecialistName(String specialistName) { this.specialistName = specialistName; }
    public String getSpecialistEmail() { return specialistEmail; }
    public void setSpecialistEmail(String specialistEmail) { this.specialistEmail = specialistEmail; }
    public RequestType getRequestType() { return requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }
    public RequestAction getActionType() { return actionType; }
    public void setActionType(RequestAction actionType) { this.actionType = actionType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getCurrentValue() { return currentValue; }
    public void setCurrentValue(String currentValue) { this.currentValue = currentValue; }
    public String getRequestedValue() { return requestedValue; }
    public void setRequestedValue(String requestedValue) { this.requestedValue = requestedValue; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }

    public static SpecialistUpdateRequestResponseBuilder builder() { return new SpecialistUpdateRequestResponseBuilder(); }

    public static class SpecialistUpdateRequestResponseBuilder {
        private Long requestId;
        private Long specialistId;
        private String specialistName;
        private String specialistEmail;
        private RequestType requestType;
        private RequestAction actionType;
        private Long targetId;
        private String currentValue;
        private String requestedValue;
        private String reason;
        private RequestStatus status;
        private String managerComment;
        private LocalDateTime createdAt;
        private LocalDateTime reviewedAt;
        private Long reviewedBy;

        public SpecialistUpdateRequestResponseBuilder requestId(Long requestId) { this.requestId = requestId; return this; }
        public SpecialistUpdateRequestResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public SpecialistUpdateRequestResponseBuilder specialistName(String specialistName) { this.specialistName = specialistName; return this; }
        public SpecialistUpdateRequestResponseBuilder specialistEmail(String specialistEmail) { this.specialistEmail = specialistEmail; return this; }
        public SpecialistUpdateRequestResponseBuilder requestType(RequestType requestType) { this.requestType = requestType; return this; }
        public SpecialistUpdateRequestResponseBuilder actionType(RequestAction actionType) { this.actionType = actionType; return this; }
        public SpecialistUpdateRequestResponseBuilder targetId(Long targetId) { this.targetId = targetId; return this; }
        public SpecialistUpdateRequestResponseBuilder currentValue(String currentValue) { this.currentValue = currentValue; return this; }
        public SpecialistUpdateRequestResponseBuilder requestedValue(String requestedValue) { this.requestedValue = requestedValue; return this; }
        public SpecialistUpdateRequestResponseBuilder reason(String reason) { this.reason = reason; return this; }
        public SpecialistUpdateRequestResponseBuilder status(RequestStatus status) { this.status = status; return this; }
        public SpecialistUpdateRequestResponseBuilder managerComment(String managerComment) { this.managerComment = managerComment; return this; }
        public SpecialistUpdateRequestResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SpecialistUpdateRequestResponseBuilder reviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; return this; }
        public SpecialistUpdateRequestResponseBuilder reviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; return this; }

        public SpecialistUpdateRequestResponse build() {
            return new SpecialistUpdateRequestResponse(requestId, specialistId, specialistName, specialistEmail,
                    requestType, actionType, targetId, currentValue, requestedValue, reason, status,
                    managerComment, createdAt, reviewedAt, reviewedBy);
        }
    }
}
