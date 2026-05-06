package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.UserRole;

public class LoginResponse {

    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private Long customerId;
    private Long specialistId;
    private Long managerId;

    public LoginResponse() {}

    public LoginResponse(Long userId, String username, String email, UserRole role,
                        Long customerId, Long specialistId, Long managerId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.customerId = customerId;
        this.specialistId = specialistId;
        this.managerId = managerId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private Long userId;
        private String username;
        private String email;
        private UserRole role;
        private Long customerId;
        private Long specialistId;
        private Long managerId;

        public LoginResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public LoginResponseBuilder username(String username) { this.username = username; return this; }
        public LoginResponseBuilder email(String email) { this.email = email; return this; }
        public LoginResponseBuilder role(UserRole role) { this.role = role; return this; }
        public LoginResponseBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
        public LoginResponseBuilder specialistId(Long specialistId) { this.specialistId = specialistId; return this; }
        public LoginResponseBuilder managerId(Long managerId) { this.managerId = managerId; return this; }
        public LoginResponse build() {
            return new LoginResponse(userId, username, email, role, customerId, specialistId, managerId);
        }
    }
}
