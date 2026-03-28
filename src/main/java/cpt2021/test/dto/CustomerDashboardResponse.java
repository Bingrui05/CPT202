package cpt2021.test.dto;

public class CustomerDashboardResponse {

    private Long customerId;
    private String message;

    public CustomerDashboardResponse(Long customerId, String message) {
        this.customerId = customerId;
        this.message = message;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getMessage() {
        return message;
    }
}