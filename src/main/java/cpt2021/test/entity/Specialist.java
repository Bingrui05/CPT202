package cpt2021.test.entity;

import jakarta.persistence.*;

@Entity
public class Specialist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String status;

    @ManyToOne
    private Level level;

    @ManyToOne
    private ExpertiseCategory expertiseCategory;

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Level getLevel() {
        return level;
    }

    public ExpertiseCategory getExpertiseCategory() {
        return expertiseCategory;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setExpertiseCategory(ExpertiseCategory expertiseCategory) {
        this.expertiseCategory = expertiseCategory;
    }
}
