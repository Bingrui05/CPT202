package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.NotNull;

public class RescheduleBookingRequest {

    @NotNull(message = "New slot ID is required")
    private Long newSlotId;

    public RescheduleBookingRequest() {}

    public RescheduleBookingRequest(Long newSlotId) {
        this.newSlotId = newSlotId;
    }

    public Long getNewSlotId() { return newSlotId; }
    public void setNewSlotId(Long newSlotId) { this.newSlotId = newSlotId; }
}
