package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.SpecialistStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UpdateSpecialistRequest {

    private Long categoryId;

    private Long levelId;

    @DecimalMin(value = "0.0", message = "Fee cannot be negative")
    private BigDecimal fee;

    private SpecialistStatus status;

    private String information;

    public UpdateSpecialistRequest() {}

    public UpdateSpecialistRequest(Long categoryId, Long levelId, BigDecimal fee,
                                   SpecialistStatus status, String information) {
        this.categoryId = categoryId;
        this.levelId = levelId;
        this.fee = fee;
        this.status = status;
        this.information = information;
    }

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
