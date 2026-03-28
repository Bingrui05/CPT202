package cpt2021.test.entity;

import jakarta.persistence.*;

@Entity
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pricingId;

    private Long levelId;     // 对应 level
    private Double basePrice; // 基础价格

    public PricingRule() {}

    public Long getPricingId() {
        return pricingId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}