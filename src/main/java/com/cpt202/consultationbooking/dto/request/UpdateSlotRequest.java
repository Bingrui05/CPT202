package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.SlotStatus;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class UpdateSlotRequest {

    private Long specialistId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;

    public UpdateSlotRequest() {}

    public UpdateSlotRequest(Long specialistId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, SlotStatus status) {
        this.specialistId = specialistId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }
}
