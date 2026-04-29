package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.SpecialistStatus;
import java.math.BigDecimal;

public class SpecialistResponse {

    private Long specialistId;
    private Long userId;
    private String username;
    private String email;
    private String categoryName;
    private String levelName;
    private SpecialistStatus status;
    private BigDecimal fee;
    private String information;

    public SpecialistResponse() {}

    public SpecialistResponse(Long specialistId, Long userId, String username, String email,
                            String categoryName, String levelName, SpecialistStatus status,
                            BigDecimal fee, String information) {
        this.specialistId = specialistId;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.categoryName = categoryName;
        this.levelName = levelName;
        this.status = status;
        this.fee = fee;
        this.information = information;
    }

    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public SpecialistStatus getStatus() { return status; }
    public void setStatus(SpecialistStatus status) { this.status = status; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }

    public static SpecialistResponseBuilder builder() {
        return new SpecialistResponseBuilder();
    }

    public static class SpecialistResponseBuilder {
        private Long specialistId;
        private Long userId;
        private String username;
        private String email;
        private String categoryName;
        private String levelName;
        private SpecialistStatus status;
        private BigDecimal fee;
        private String information;

        public SpecialistResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public SpecialistResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public SpecialistResponseBuilder username(String username) { this.username = username; return this; }
        public SpecialistResponseBuilder email(String email) { this.email = email; return this; }
        public SpecialistResponseBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public SpecialistResponseBuilder levelName(String levelName) { this.levelName = levelName; return this; }
        public SpecialistResponseBuilder status(SpecialistStatus status) { this.status = status; return this; }
        public SpecialistResponseBuilder fee(BigDecimal fee) { this.fee = fee; return this; }
        public SpecialistResponseBuilder information(String information) { this.information = information; return this; }
        public SpecialistResponse build() {
            return new SpecialistResponse(specialistId, userId, username, email, categoryName, levelName, status, fee, information);
        }
    }
}
