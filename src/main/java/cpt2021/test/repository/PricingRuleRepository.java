package cpt2021.test.repository;

import cpt2021.test.entity.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    Optional<PricingRule> findByLevelId(Long levelId);
}