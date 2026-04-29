package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateSlotRequest {

    @NotNull(message = "Specialist ID is required")
    private Long specialistId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    public CreateSlotRequest() {}

    public CreateSlotRequest(Long specialistId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.specialistId = specialistId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
