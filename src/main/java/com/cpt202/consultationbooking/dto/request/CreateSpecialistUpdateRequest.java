package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.RequestAction;
import com.cpt202.consultationbooking.enums.RequestType;
import jakarta.validation.constraints.NotNull;

public class CreateSpecialistUpdateRequest {

    @NotNull(message = "Specialist ID is required")
    private Long specialistId;

    @NotNull(message = "Request type is required")
    private RequestType requestType;

    @NotNull(message = "Action type is required")
    private RequestAction actionType;

    private Long targetId;

    private String currentValue;

    @NotNull(message = "Requested value is required")
    private String requestedValue;

    private String reason;

    public CreateSpecialistUpdateRequest() {}

    public CreateSpecialistUpdateRequest(Long specialistId, RequestType requestType, RequestAction actionType,
            Long targetId, String currentValue, String requestedValue, String reason) {
        this.specialistId = specialistId;
        this.requestType = requestType;
        this.actionType = actionType;
        this.targetId = targetId;
        this.currentValue = currentValue;
        this.requestedValue = requestedValue;
        this.reason = reason;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
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
}
