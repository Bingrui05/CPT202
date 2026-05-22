package com.cpt202.consultationbooking.entity;

import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestStatus;
import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "specialist_update_requests")
public class SpecialistUpdateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialistUpdateRequestType requestType;

    @Column(nullable = false, length = 100)
    private String fieldName;

    @Column(length = 2000)
    private String oldValue;

    @Column(nullable = false, length = 2000)
    private String newValue;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialistUpdateRequestStatus status;

    @Column(length = 1000)
    private String managerComment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    public SpecialistUpdateRequest() {}

    public SpecialistUpdateRequest(Long requestId, Specialist specialist, SpecialistUpdateRequestType requestType,
                                   String fieldName, String oldValue, String newValue, String reason,
                                   SpecialistUpdateRequestStatus status, String managerComment,
                                   LocalDateTime createdAt, LocalDateTime reviewedAt) {
        this.requestId = requestId;
        this.specialist = specialist;
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
    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }
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

    public static SpecialistUpdateRequestBuilder builder() { return new SpecialistUpdateRequestBuilder(); }

    public static class SpecialistUpdateRequestBuilder {
        private Long requestId;
        private Specialist specialist;
        private SpecialistUpdateRequestType requestType;
        private String fieldName;
        private String oldValue;
        private String newValue;
        private String reason;
        private SpecialistUpdateRequestStatus status;
        private String managerComment;
        private LocalDateTime createdAt;
        private LocalDateTime reviewedAt;

        public SpecialistUpdateRequestBuilder requestId(Long requestId) { this.requestId = requestId; return this; }
        public SpecialistUpdateRequestBuilder specialist(Specialist specialist) { this.specialist = specialist; return this; }
        public SpecialistUpdateRequestBuilder requestType(SpecialistUpdateRequestType requestType) { this.requestType = requestType; return this; }
        public SpecialistUpdateRequestBuilder fieldName(String fieldName) { this.fieldName = fieldName; return this; }
        public SpecialistUpdateRequestBuilder oldValue(String oldValue) { this.oldValue = oldValue; return this; }
        public SpecialistUpdateRequestBuilder newValue(String newValue) { this.newValue = newValue; return this; }
        public SpecialistUpdateRequestBuilder reason(String reason) { this.reason = reason; return this; }
        public SpecialistUpdateRequestBuilder status(SpecialistUpdateRequestStatus status) { this.status = status; return this; }
        public SpecialistUpdateRequestBuilder managerComment(String managerComment) { this.managerComment = managerComment; return this; }
        public SpecialistUpdateRequestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SpecialistUpdateRequestBuilder reviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; return this; }

        public SpecialistUpdateRequest build() {
            return new SpecialistUpdateRequest(requestId, specialist, requestType, fieldName, oldValue,
                    newValue, reason, status, managerComment, createdAt, reviewedAt);
        }
    }
}
