package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateSpecialistRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Level ID is required")
    private Long levelId;

    @NotNull(message = "Fee is required")
    private BigDecimal fee;

    private String information;

    public CreateSpecialistRequest() {}

    public CreateSpecialistRequest(Long userId, Long categoryId, Long levelId, BigDecimal fee, String information) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.levelId = levelId;
        this.fee = fee;
        this.information = information;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getLevelId() { return levelId; }
    public void setLevelId(Long levelId) { this.levelId = levelId; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }
}
