package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestStatus;
import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestType;
import java.time.LocalDateTime;

public class SpecialistUpdateRequestResponse {

    private Long requestId;
    private Long specialistId;
    private String specialistName;
    private SpecialistUpdateRequestType requestType;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String reason;
    private SpecialistUpdateRequestStatus status;
    private String managerComment;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    public SpecialistUpdateRequestResponse() {}

    public SpecialistUpdateRequestResponse(Long requestId, Long specialistId, String specialistName,
                                           SpecialistUpdateRequestType requestType, String fieldName,
                                           String oldValue, String newValue, String reason,
                                           SpecialistUpdateRequestStatus status, String managerComment,
                                           LocalDateTime createdAt, LocalDateTime reviewedAt) {
        this.requestId = requestId;
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.requestType = requestType;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.reason = reason;
        this.status = status;
        this.managerComment = managerComment;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
    }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public String getSpecialistName() { return specialistName; }
    public void setSpecialistName(String specialistName) { this.specialistName = specialistName; }
    public SpecialistUpdateRequestType getRequestType() { return requestType; }
    public void setRequestType(SpecialistUpdateRequestType requestType) { this.requestType = requestType; }
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public SpecialistUpdateRequestStatus getStatus() { return status; }
    public void setStatus(SpecialistUpdateRequestStatus status) { this.status = status; }
    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public static SpecialistUpdateRequestResponseBuilder builder() {
        return new SpecialistUpdateRequestResponseBuilder();
    }

    public static class SpecialistUpdateRequestResponseBuilder {
        private Long requestId;
        private Long specialistId;
        private String specialistName;
        private SpecialistUpdateRequestType requestType;
        private String fieldName;
        private String oldValue;
        private String newValue;
        private String reason;
        private SpecialistUpdateRequestStatus status;
        private String managerComment;
        private LocalDateTime createdAt;
        private LocalDateTime reviewedAt;

        public SpecialistUpdateRequestResponseBuilder requestId(Long requestId) { this.requestId = requestId; return this; }
        public SpecialistUpdateRequestResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public SpecialistUpdateRequestResponseBuilder specialistName(String specialistName) { this.specialistName = specialistName; return this; }
        public SpecialistUpdateRequestResponseBuilder requestType(SpecialistUpdateRequestType requestType) { this.requestType = requestType; return this; }
        public SpecialistUpdateRequestResponseBuilder fieldName(String fieldName) { this.fieldName = fieldName; return this; }
        public SpecialistUpdateRequestResponseBuilder oldValue(String oldValue) { this.oldValue = oldValue; return this; }
        public SpecialistUpdateRequestResponseBuilder newValue(String newValue) { this.newValue = newValue; return this; }
        public SpecialistUpdateRequestResponseBuilder reason(String reason) { this.reason = reason; return this; }
        public SpecialistUpdateRequestResponseBuilder status(SpecialistUpdateRequestStatus status) { this.status = status; return this; }
        public SpecialistUpdateRequestResponseBuilder managerComment(String managerComment) { this.managerComment = managerComment; return this; }
        public SpecialistUpdateRequestResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SpecialistUpdateRequestResponseBuilder reviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; return this; }

        public SpecialistUpdateRequestResponse build() {
            return new SpecialistUpdateRequestResponse(requestId, specialistId, specialistName, requestType,
                    fieldName, oldValue, newValue, reason, status, managerComment, createdAt, reviewedAt);
        }
    }
}
