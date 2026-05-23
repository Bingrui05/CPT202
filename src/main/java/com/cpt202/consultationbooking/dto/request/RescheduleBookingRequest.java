package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RescheduleBookingRequest {

    @NotNull(message = "New slot ID is required")
    private Long newSlotId;

    @NotNull(message = "New appointment date is required")
    private LocalDate newAppointmentDate;

    public RescheduleBookingRequest() {}

    public RescheduleBookingRequest(Long newSlotId, LocalDate newAppointmentDate) {
        this.newSlotId = newSlotId;
        this.newAppointmentDate = newAppointmentDate;
    }

    public Long getNewSlotId() { return newSlotId; }
    public void setNewSlotId(Long newSlotId) { this.newSlotId = newSlotId; }
    public LocalDate getNewAppointmentDate() { return newAppointmentDate; }
    public void setNewAppointmentDate(LocalDate newAppointmentDate) { this.newAppointmentDate = newAppointmentDate; }
}
