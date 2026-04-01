package cpt2021.test.service;

import cpt2021.test.dto.BookingSummary;
import cpt2021.test.dto.CustomerDashboardResponse;
import cpt2021.test.dto.ScheduleItem;
import cpt2021.test.dto.SpecialistDashboardResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    public CustomerDashboardResponse getCustomerDashboard(Long id) {
        // 模拟：只有 customerId = 1 才有数据
    if (!id.equals(1L)) {
        return new CustomerDashboardResponse(
                id,
                List.of(),
                List.of(),
                List.of()
        );
    }

    List<BookingSummary> upcoming = Arrays.asList(
            new BookingSummary(1L, "Dr. Smith", "2026-03-30 10:00", "Pending")
    );

    List<BookingSummary> completed = Arrays.asList(
            new BookingSummary(2L, "Dr. Lee", "2026-04-02 14:00", "Completed")
    );

    List<BookingSummary> cancelled = List.of(); // 暂时没有

    return new CustomerDashboardResponse(
            id,
            upcoming,
            completed,
            cancelled
    );
}

    public SpecialistDashboardResponse getSpecialistDashboard(Long id) {
       // 模拟：只有 specialistId = 1 才有数据
    if (!id.equals(1L)) {
        return new SpecialistDashboardResponse(
                id,
                List.of(),
                List.of(),
                List.of()
        );
    }
        List<ScheduleItem> pendingRequests = Arrays.asList(
            new ScheduleItem(1L, "Alice", "2026-03-30 10:00", "Pending"),
            new ScheduleItem(2L, "Bob", "2026-03-31 11:00", "Pending")
    );

    List<ScheduleItem> upcomingSessions = Arrays.asList(
            new ScheduleItem(3L, "Cindy", "2026-04-02 14:00", "Confirmed")
    );

    List<ScheduleItem> completedSessions = Arrays.asList(
            new ScheduleItem(4L, "David", "2026-03-20 09:00", "Completed")
    );

    return new SpecialistDashboardResponse(
            id,
            pendingRequests,
            upcomingSessions,
            completedSessions
    );

    }
}