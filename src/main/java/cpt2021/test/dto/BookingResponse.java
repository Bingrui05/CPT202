package cpt2021.test.dto;

public class BookingResponse {
    private Long id;
    private Long customerId;
    private Long specialistId;
    private Long slotId;
    private Long pricingId;
    private String status;

    public BookingResponse(Long id, Long customerId, Long specialistId, Long slotId, Long pricingId, String status) {
        this.id = id;
        this.customerId = customerId;
        this.specialistId = specialistId;
        this.slotId = slotId;
        this.pricingId = pricingId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public Long getPricingId() {
        return pricingId;
    }

    public String getStatus() {
        return status;
    }
}