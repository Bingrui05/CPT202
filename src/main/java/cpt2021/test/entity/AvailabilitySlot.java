package cpt2021.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AvailabilitySlot {

    @Id
    private Long id;

    private boolean available;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}