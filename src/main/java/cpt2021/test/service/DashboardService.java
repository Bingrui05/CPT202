package cpt2021.test.service;

import cpt2021.test.dto.CustomerDashboardResponse;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public CustomerDashboardResponse getCustomerDashboard(Long id) {
        return new CustomerDashboardResponse(id, "Mock dashboard data");
    }

    public CustomerDashboardResponse getSpecialistDashboard(Long id) {
        return new CustomerDashboardResponse(id, "Mock specialist dashboard data");
    }
}