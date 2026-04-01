package cpt2021.test.dto;

import java.util.List;

public class CustomerDashboardResponse {
    private Long customerId;
    private List<BookingSummary> upcomingBookings;
private List<BookingSummary> completedBookings;
private List<BookingSummary> cancelledBookings;

    public CustomerDashboardResponse(Long customerId,
                                 List<BookingSummary> upcomingBookings,
                                 List<BookingSummary> completedBookings,
                                 List<BookingSummary> cancelledBookings) {
    this.customerId = customerId;
    this.upcomingBookings = upcomingBookings;
    this.completedBookings = completedBookings;
    this.cancelledBookings = cancelledBookings;
}

    public Long getCustomerId() {
        return customerId;
    }

    public List<BookingSummary> getUpcomingBookings() {
    return upcomingBookings;
}

public List<BookingSummary> getCompletedBookings() {
    return completedBookings;
}

public List<BookingSummary> getCancelledBookings() {
    return cancelledBookings;
}
}