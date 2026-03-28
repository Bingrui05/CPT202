package cpt2021.test.service;

import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public String getCustomerDashboard(Long id) {
        return "Mock customer dashboard data for user " + id;
    }

    public String getSpecialistDashboard(Long id) {
        return "Mock specialist dashboard data for specialist " + id;
    }
}