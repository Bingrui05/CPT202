package cpt2021.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {

    @Id
    private Long id;

    private String status;

    @OneToOne
    private AvailabilitySlot availabilitySlot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AvailabilitySlot getAvailabilitySlot() {
        return availabilitySlot;
    }

    public void setAvailabilitySlot(AvailabilitySlot availabilitySlot) {
        this.availabilitySlot = availabilitySlot;
    }
}