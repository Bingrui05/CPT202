package cpt2021.test.controller;

import cpt2021.test.service.PricingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/pricing/{levelId}")
    public double getBasePrice(@PathVariable Long levelId) {
        return pricingService.getBasePriceByLevelId(levelId);
    }
}