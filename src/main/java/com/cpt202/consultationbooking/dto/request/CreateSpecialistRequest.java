package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.SpecialistStatus;
import jakarta.validation.constraints.DecimalMin;
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
    @DecimalMin(value = "0.0", message = "Fee cannot be negative")
    private BigDecimal fee;

    @NotNull(message = "Status is required")
    private SpecialistStatus status;

    private String information;

    public CreateSpecialistRequest() {}

    public CreateSpecialistRequest(Long userId, Long categoryId, Long levelId, BigDecimal fee,
                                  SpecialistStatus status, String information) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.levelId = levelId;
        this.fee = fee;
        this.status = status;
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
    public SpecialistStatus getStatus() { return status; }
    public void setStatus(SpecialistStatus status) { this.status = status; }
    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }
}
