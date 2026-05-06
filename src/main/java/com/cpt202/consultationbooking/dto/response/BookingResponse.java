package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingResponse {

    private Long bookingId;
    private Long customerId;
    private String customerName;
    private Long specialistId;
    private String specialistName;
    private Long slotId;
    private LocalDate appointmentDate;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private String topic;
    private String notes;
    private BookingStatus status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookingResponse() {}

    public BookingResponse(Long bookingId, Long customerId, String customerName, Long specialistId,
                          String specialistName, Long slotId, LocalDate appointmentDate,
                          LocalTime slotStartTime, LocalTime slotEndTime, String topic,
                          String notes, BookingStatus status, BigDecimal price,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.specialistId = specialistId;
        this.specialistName = specialistName;
        this.slotId = slotId;
        this.appointmentDate = appointmentDate;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.topic = topic;
        this.notes = notes;
        this.status = status;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public String getSpecialistName() { return specialistName; }
    public void setSpecialistName(String specialistName) { this.specialistName = specialistName; }
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(LocalTime slotStartTime) { this.slotStartTime = slotStartTime; }
    public LocalTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(LocalTime slotEndTime) { this.slotEndTime = slotEndTime; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static BookingResponseBuilder builder() {
        return new BookingResponseBuilder();
    }

    public static class BookingResponseBuilder {
        private Long bookingId;
        private Long customerId;
        private String customerName;
        private Long specialistId;
        private String specialistName;
        private Long slotId;
        private LocalDate appointmentDate;
        private LocalTime slotStartTime;
        private LocalTime slotEndTime;
        private String topic;
        private String notes;
        private BookingStatus status;
        private BigDecimal price;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BookingResponseBuilder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public BookingResponseBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
        public BookingResponseBuilder customerName(String customerName) { this.customerName = customerName; return this; }
        public BookingResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public BookingResponseBuilder specialistName(String specialistName) { this.specialistName = specialistName; return this; }
        public BookingResponseBuilder slotId(Long slotId) { this.slotId = slotId; return this; }
        public BookingResponseBuilder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public BookingResponseBuilder slotStartTime(LocalTime slotStartTime) { this.slotStartTime = slotStartTime; return this; }
        public BookingResponseBuilder slotEndTime(LocalTime slotEndTime) { this.slotEndTime = slotEndTime; return this; }
        public BookingResponseBuilder topic(String topic) { this.topic = topic; return this; }
        public BookingResponseBuilder notes(String notes) { this.notes = notes; return this; }
        public BookingResponseBuilder status(BookingStatus status) { this.status = status; return this; }
        public BookingResponseBuilder price(BigDecimal price) { this.price = price; return this; }
        public BookingResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BookingResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public BookingResponse build() {
            return new BookingResponse(bookingId, customerId, customerName, specialistId, specialistName,
                    slotId, appointmentDate, slotStartTime, slotEndTime, topic, notes, status, price, createdAt, updatedAt);
        }
    }
}
