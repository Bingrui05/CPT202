package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.SlotStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class SlotResponse {

    private Long slotId;
    private Long specialistId;
    private String specialistName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;

    public SlotResponse() {}

    public SlotResponse(Long slotId, Long specialistId, String specialistName,
                       LocalDate date, LocalTime startTime, LocalTime endTime, SlotStatus status) {
        this.slotId = slotId;
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public String getSpecialistName() { return specialistName; }
    public void setSpecialistName(String specialistName) { this.specialistName = specialistName; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }

    public static SlotResponseBuilder builder() {
        return new SlotResponseBuilder();
    }

    public static class SlotResponseBuilder {
        private Long slotId;
        private Long specialistId;
        private String specialistName;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private SlotStatus status;

        public SlotResponseBuilder slotId(Long slotId) { this.slotId = slotId; return this; }
        public SlotResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public SlotResponseBuilder specialistName(String specialistName) { this.specialistName = specialistName; return this; }
        public SlotResponseBuilder date(LocalDate date) { this.date = date; return this; }
        public SlotResponseBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public SlotResponseBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public SlotResponseBuilder status(SlotStatus status) { this.status = status; return this; }
        public SlotResponse build() {
            return new SlotResponse(slotId, specialistId, specialistName, date, startTime, endTime, status);
        }
    }
}
