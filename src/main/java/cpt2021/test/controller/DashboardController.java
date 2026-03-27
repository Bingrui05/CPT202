package cpt2021.test.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    // Customer dashboard
    @GetMapping("/customer/{id}")
    public String getCustomerDashboard(@PathVariable Long id) {
        return "Customer dashboard for user " + id;
    }

    // Specialist dashboard
    @GetMapping("/specialist/{id}")
    public String getSpecialistDashboard(@PathVariable Long id) {
        return "Specialist dashboard for specialist " + id;
    }
}