package com.cpt202.consultationbooking.entity;

import com.cpt202.consultationbooking.enums.RequestAction;
import com.cpt202.consultationbooking.enums.RequestStatus;
import com.cpt202.consultationbooking.enums.RequestType;
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
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private RequestAction actionType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "current_value", columnDefinition = "TEXT")
    private String currentValue;

    @Column(name = "requested_value", columnDefinition = "TEXT", nullable = false)
    private String requestedValue;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(name = "manager_comment", length = 500)
    private String managerComment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    public SpecialistUpdateRequest() {}

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }
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

    public static SpecialistUpdateRequestBuilder builder() { return new SpecialistUpdateRequestBuilder(); }

    public static class SpecialistUpdateRequestBuilder {
        private Long requestId;
        private Specialist specialist;
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

        public SpecialistUpdateRequestBuilder requestId(Long requestId) { this.requestId = requestId; return this; }
        public SpecialistUpdateRequestBuilder specialist(Specialist specialist) { this.specialist = specialist; return this; }
        public SpecialistUpdateRequestBuilder requestType(RequestType requestType) { this.requestType = requestType; return this; }
        public SpecialistUpdateRequestBuilder actionType(RequestAction actionType) { this.actionType = actionType; return this; }
        public SpecialistUpdateRequestBuilder targetId(Long targetId) { this.targetId = targetId; return this; }
        public SpecialistUpdateRequestBuilder currentValue(String currentValue) { this.currentValue = currentValue; return this; }
        public SpecialistUpdateRequestBuilder requestedValue(String requestedValue) { this.requestedValue = requestedValue; return this; }
        public SpecialistUpdateRequestBuilder reason(String reason) { this.reason = reason; return this; }
        public SpecialistUpdateRequestBuilder status(RequestStatus status) { this.status = status; return this; }
        public SpecialistUpdateRequestBuilder managerComment(String managerComment) { this.managerComment = managerComment; return this; }
        public SpecialistUpdateRequestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SpecialistUpdateRequestBuilder reviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; return this; }
        public SpecialistUpdateRequestBuilder reviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; return this; }

        public SpecialistUpdateRequest build() {
            SpecialistUpdateRequest req = new SpecialistUpdateRequest();
            req.requestId = this.requestId;
            req.specialist = this.specialist;
            req.requestType = this.requestType;
            req.actionType = this.actionType;
            req.targetId = this.targetId;
            req.currentValue = this.currentValue;
            req.requestedValue = this.requestedValue;
            req.reason = this.reason;
            req.status = this.status;
            req.managerComment = this.managerComment;
            req.createdAt = this.createdAt;
            req.reviewedAt = this.reviewedAt;
            req.reviewedBy = this.reviewedBy;
            return req;
        }
    }
}
