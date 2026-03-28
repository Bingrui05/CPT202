package cpt2021.test.service;

import cpt2021.test.dto.BookingSummary;
import cpt2021.test.dto.CustomerDashboardResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    public CustomerDashboardResponse getCustomerDashboard(Long id) {
        List<BookingSummary> bookings = Arrays.asList(
                new BookingSummary(1L, "Dr. Smith", "2026-03-30 10:00", "Pending"),
                new BookingSummary(2L, "Dr. Lee", "2026-04-02 14:00", "Completed")
        );

        return new CustomerDashboardResponse(id, bookings);
    }

    public String getSpecialistDashboard(Long id) {
        return "Mock specialist dashboard data for specialist " + id;
    }
}