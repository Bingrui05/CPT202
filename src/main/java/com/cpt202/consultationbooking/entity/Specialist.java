package com.cpt202.consultationbooking.entity;

import com.cpt202.consultationbooking.enums.SpecialistStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specialists")
public class Specialist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialistId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpertiseCategory category;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialistStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(length = 1000)
    private String information;

    @OneToMany(mappedBy = "specialist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvailabilitySlot> slotList = new ArrayList<>();

    public Specialist() {}

    public Specialist(Long specialistId, User user, ExpertiseCategory category, Level level,
                     SpecialistStatus status, BigDecimal fee, String information) {
        this.specialistId = specialistId;
        this.user = user;
        this.category = category;
        this.level = level;
        this.status = status;
        this.fee = fee;
        this.information = information;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public ExpertiseCategory getCategory() { return category; }
    public void setCategory(ExpertiseCategory category) { this.category = category; }
    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }
    public SpecialistStatus getStatus() { return status; }
    public void setStatus(SpecialistStatus status) { this.status = status; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }
    public List<AvailabilitySlot> getSlotList() { return slotList; }
    public void setSlotList(List<AvailabilitySlot> slotList) { this.slotList = slotList; }

    public static SpecialistBuilder builder() { return new SpecialistBuilder(); }

    public static class SpecialistBuilder {
        private Long specialistId;
        private User user;
        private ExpertiseCategory category;
        private Level level;
        private SpecialistStatus status;
        private BigDecimal fee;
        private String information;

        public SpecialistBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public SpecialistBuilder user(User user) { this.user = user; return this; }
        public SpecialistBuilder category(ExpertiseCategory category) { this.category = category; return this; }
        public SpecialistBuilder level(Level level) { this.level = level; return this; }
        public SpecialistBuilder status(SpecialistStatus status) { this.status = status; return this; }
        public SpecialistBuilder fee(BigDecimal fee) { this.fee = fee; return this; }
        public SpecialistBuilder information(String information) { this.information = information; return this; }
        public Specialist build() {
            return new Specialist(specialistId, user, category, level, status, fee, information);
        }
    }
}
