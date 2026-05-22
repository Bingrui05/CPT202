package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateBookingRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Specialist ID is required")
    private Long specialistId;

    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Topic is required")
    private String topic;

    private String notes;

    public CreateBookingRequest() {}

    public CreateBookingRequest(Long customerId, Long specialistId, Long slotId, String topic, String notes) {
        this.customerId = customerId;
        this.specialistId = specialistId;
        this.slotId = slotId;
        this.topic = topic;
        this.notes = notes;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
