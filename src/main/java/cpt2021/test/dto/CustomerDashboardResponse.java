package cpt2021.test.dto;

import java.util.List;

public class CustomerDashboardResponse {
    private Long customerId;
    private List<BookingSummary> bookings;

    public CustomerDashboardResponse(Long customerId, List<BookingSummary> bookings) {
        this.customerId = customerId;
        this.bookings = bookings;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<BookingSummary> getBookings() {
        return bookings;
    }
}