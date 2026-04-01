package cpt2021.test.service;

import cpt2021.test.entity.PricingRule;
import cpt2021.test.repository.PricingRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PricingService {

    private final PricingRuleRepository pricingRuleRepository;

    public PricingService(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }

    public double getBasePriceByLevelId(Long levelId) {
        Optional<PricingRule> pricingRule = pricingRuleRepository.findByLevelId(levelId);

        if (pricingRule.isPresent()) {
            return pricingRule.get().getBasePrice();
        }

        throw new RuntimeException("Pricing rule not found for levelId: " + levelId);
    }
}