package com.cpt202.consultationbooking.entity;

import com.cpt202.consultationbooking.enums.SlotStatus;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "availability_slots")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    public AvailabilitySlot() {}

    public AvailabilitySlot(Long slotId, Specialist specialist, DayOfWeek dayOfWeek, LocalTime startTime,
                           LocalTime endTime, SlotStatus status) {
        this.slotId = slotId;
        this.specialist = specialist;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }

    public static AvailabilitySlotBuilder builder() { return new AvailabilitySlotBuilder(); }

    public static class AvailabilitySlotBuilder {
        private Long slotId;
        private Specialist specialist;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private SlotStatus status;

        public AvailabilitySlotBuilder slotId(Long slotId) { this.slotId = slotId; return this; }
        public AvailabilitySlotBuilder specialist(Specialist specialist) { this.specialist = specialist; return this; }
        public AvailabilitySlotBuilder dayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; return this; }
        public AvailabilitySlotBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public AvailabilitySlotBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public AvailabilitySlotBuilder status(SlotStatus status) { this.status = status; return this; }
        public AvailabilitySlot build() {
            return new AvailabilitySlot(slotId, specialist, dayOfWeek, startTime, endTime, status);
        }
    }
}
