package cpt2021.test.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "specialist")
public class Specialist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 32)
    private String status;

    @Column(length = 255)
    private String email;

    @Column(length = 32)
    private String phone;

    @Column(length = 2000)
    private String bio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expertise_category_id", nullable = false)
    private ExpertiseCategory expertiseCategory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public Specialist() {
    }

    public Specialist(String name, String email, String phone, String bio,
                      ExpertiseCategory expertiseCategory, Level level) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.expertiseCategory = expertiseCategory;
        this.level = level;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 主字段：name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 兼容旧代码：displayName
    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.name = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ExpertiseCategory getExpertiseCategory() {
        return expertiseCategory;
    }

    public void setExpertiseCategory(ExpertiseCategory expertiseCategory) {
        this.expertiseCategory = expertiseCategory;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}