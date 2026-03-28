package cpt2021.test.controller;

import cpt2021.test.dto.CustomerDashboardResponse;
import cpt2021.test.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/customer/{id}")
    public CustomerDashboardResponse getCustomerDashboard(@PathVariable Long id) {
        return dashboardService.getCustomerDashboard(id);
    }

    @GetMapping("/specialist/{id}")
    public String getSpecialistDashboard(@PathVariable Long id) {
        return dashboardService.getSpecialistDashboard(id);
    }
}