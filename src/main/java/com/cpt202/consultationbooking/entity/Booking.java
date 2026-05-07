package com.cpt202.consultationbooking.entity;

import com.cpt202.consultationbooking.enums.BookingStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private AvailabilitySlot slot;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private String topic;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Booking() {}

    public Booking(Long bookingId, Customer customer, Specialist specialist, AvailabilitySlot slot,
                   LocalDate appointmentDate, String topic, String notes, BookingStatus status, BigDecimal price,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.specialist = specialist;
        this.slot = slot;
        this.appointmentDate = appointmentDate;
        this.topic = topic;
        this.notes = notes;
        this.status = status;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }
    public AvailabilitySlot getSlot() { return slot; }
    public void setSlot(AvailabilitySlot slot) { this.slot = slot; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
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

    public static BookingBuilder builder() { return new BookingBuilder(); }

    public static class BookingBuilder {
        private Long bookingId;
        private Customer customer;
        private Specialist specialist;
        private AvailabilitySlot slot;
        private LocalDate appointmentDate;
        private String topic;
        private String notes;
        private BookingStatus status;
        private BigDecimal price;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BookingBuilder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public BookingBuilder customer(Customer customer) { this.customer = customer; return this; }
        public BookingBuilder specialist(Specialist specialist) { this.specialist = specialist; return this; }
        public BookingBuilder slot(AvailabilitySlot slot) { this.slot = slot; return this; }
        public BookingBuilder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public BookingBuilder topic(String topic) { this.topic = topic; return this; }
        public BookingBuilder notes(String notes) { this.notes = notes; return this; }
        public BookingBuilder status(BookingStatus status) { this.status = status; return this; }
        public BookingBuilder price(BigDecimal price) { this.price = price; return this; }
        public BookingBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BookingBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Booking build() {
            return new Booking(bookingId, customer, specialist, slot, appointmentDate, topic, notes, status, price, createdAt, updatedAt);
        }
    }
}
