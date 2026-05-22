package com.cpt202.consultationbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "expertise_categories")
public class ExpertiseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    public ExpertiseCategory() {}

    public ExpertiseCategory(Long categoryId, String name, String status) {
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    public static ExpertiseCategoryBuilder builder() { return new ExpertiseCategoryBuilder(); }

    public static class ExpertiseCategoryBuilder {
        private Long categoryId;
        private String name;
        private String status;

        public ExpertiseCategoryBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public ExpertiseCategoryBuilder name(String name) { this.name = name; return this; }
        public ExpertiseCategoryBuilder status(String status) { this.status = status; return this; }
        public ExpertiseCategory build() { return new ExpertiseCategory(categoryId, name, status); }
    }
}
