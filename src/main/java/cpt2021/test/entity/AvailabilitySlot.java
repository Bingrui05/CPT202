package cpt2021.test.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "availability_slots")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long specialistId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status; // AVAILABLE, BOOKED, CANCELLED

    public AvailabilitySlot() {
    }

    public AvailabilitySlot(Long id, Long specialistId, LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.id = id;
        this.specialistId = specialistId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 兼容 booking-reschedule 里可能调用的 available 逻辑
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status);
    }

    public void setAvailable(boolean available) {
        this.status = available ? "AVAILABLE" : "BOOKED";
    }
}