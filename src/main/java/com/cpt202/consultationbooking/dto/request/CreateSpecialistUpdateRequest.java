package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateSpecialistUpdateRequest {

    @NotNull(message = "Specialist ID is required")
    private Long specialistId;

    @NotNull(message = "Request type is required")
    private SpecialistUpdateRequestType requestType;

    @NotBlank(message = "Field name is required")
    @Size(max = 100, message = "Field name must not exceed 100 characters")
    private String fieldName;

    @Size(max = 2000, message = "Old value must not exceed 2000 characters")
    private String oldValue;

    @NotBlank(message = "New value is required")
    @Size(max = 2000, message = "New value must not exceed 2000 characters")
    private String newValue;

    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    private String reason;

    public CreateSpecialistUpdateRequest() {}

    public CreateSpecialistUpdateRequest(Long specialistId, SpecialistUpdateRequestType requestType,
                                         String fieldName, String oldValue, String newValue, String reason) {
        this.specialistId = specialistId;
        this.requestType = requestType;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.reason = reason;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
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
}
