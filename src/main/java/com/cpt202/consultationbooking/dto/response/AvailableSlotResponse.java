package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.SlotStatus;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class AvailableSlotResponse {

    private Long slotId;
    private Long specialistId;
    private String specialistName;
    private LocalDate appointmentDate;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;
    private String displayLabel;
    private String occurrenceStatus;
    private Long bookingId;
    private boolean bookable;

    public AvailableSlotResponse() {}

    public AvailableSlotResponse(Long slotId, Long specialistId, String specialistName,
                                LocalDate appointmentDate, DayOfWeek dayOfWeek,
                                LocalTime startTime, LocalTime endTime, SlotStatus status, String displayLabel,
                                String occurrenceStatus, Long bookingId, boolean bookable) {
        this.slotId = slotId;
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.appointmentDate = appointmentDate;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.displayLabel = displayLabel;
        this.occurrenceStatus = occurrenceStatus;
        this.bookingId = bookingId;
        this.bookable = bookable;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public String getSpecialistName() { return specialistName; }
    public void setSpecialistName(String specialistName) { this.specialistName = specialistName; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }
    public String getDisplayLabel() { return displayLabel; }
    public void setDisplayLabel(String displayLabel) { this.displayLabel = displayLabel; }
    public String getOccurrenceStatus() { return occurrenceStatus; }
    public void setOccurrenceStatus(String occurrenceStatus) { this.occurrenceStatus = occurrenceStatus; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public boolean isBookable() { return bookable; }
    public void setBookable(boolean bookable) { this.bookable = bookable; }

    public static AvailableSlotResponseBuilder builder() {
        return new AvailableSlotResponseBuilder();
    }

    public static class AvailableSlotResponseBuilder {
        private Long slotId;
        private Long specialistId;
        private String specialistName;
        private LocalDate appointmentDate;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private SlotStatus status;
        private String displayLabel;
        private String occurrenceStatus;
        private Long bookingId;
        private boolean bookable;

        public AvailableSlotResponseBuilder slotId(Long slotId) { this.slotId = slotId; return this; }
        public AvailableSlotResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public AvailableSlotResponseBuilder specialistName(String specialistName) { this.specialistName = specialistName; return this; }
        public AvailableSlotResponseBuilder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public AvailableSlotResponseBuilder dayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; return this; }
        public AvailableSlotResponseBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public AvailableSlotResponseBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public AvailableSlotResponseBuilder status(SlotStatus status) { this.status = status; return this; }
        public AvailableSlotResponseBuilder displayLabel(String displayLabel) { this.displayLabel = displayLabel; return this; }
        public AvailableSlotResponseBuilder occurrenceStatus(String occurrenceStatus) { this.occurrenceStatus = occurrenceStatus; return this; }
        public AvailableSlotResponseBuilder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public AvailableSlotResponseBuilder bookable(boolean bookable) { this.bookable = bookable; return this; }
        public AvailableSlotResponse build() {
            return new AvailableSlotResponse(slotId, specialistId, specialistName, appointmentDate, dayOfWeek, startTime, endTime, status, displayLabel, occurrenceStatus, bookingId, bookable);
        }
    }
}
