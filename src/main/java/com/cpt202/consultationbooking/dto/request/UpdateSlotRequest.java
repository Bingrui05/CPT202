package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.SlotStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateSlotRequest {

    private Long specialistId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;

    public UpdateSlotRequest() {}

    public UpdateSlotRequest(Long specialistId, LocalDate date, LocalTime startTime, LocalTime endTime, SlotStatus status) {
        this.specialistId = specialistId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }
}
