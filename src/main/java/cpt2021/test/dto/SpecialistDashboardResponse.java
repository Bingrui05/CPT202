package cpt2021.test.dto;

import java.util.List;

public class SpecialistDashboardResponse {
    private Long specialistId;
    private List<ScheduleItem> pendingRequests;
    private List<ScheduleItem> upcomingSessions;
    private List<ScheduleItem> completedSessions;

    public SpecialistDashboardResponse(Long specialistId,
                                       List<ScheduleItem> pendingRequests,
                                       List<ScheduleItem> upcomingSessions,
                                       List<ScheduleItem> completedSessions) {
        this.specialistId = specialistId;
        this.pendingRequests = pendingRequests;
        this.upcomingSessions = upcomingSessions;
        this.completedSessions = completedSessions;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public List<ScheduleItem> getPendingRequests() {
        return pendingRequests;
    }

    public List<ScheduleItem> getUpcomingSessions() {
        return upcomingSessions;
    }

    public List<ScheduleItem> getCompletedSessions() {
        return completedSessions;
    }
}