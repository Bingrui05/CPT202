package com.cpt202.consultationbooking.dto.response;

import com.cpt202.consultationbooking.enums.UserRole;

public class LoginResponse {

    private Long userId;
    private String username;
    private UserRole role;

    public LoginResponse() {}

    public LoginResponse(Long userId, String username, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private Long userId;
        private String username;
        private UserRole role;

        public LoginResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public LoginResponseBuilder username(String username) { this.username = username; return this; }
        public LoginResponseBuilder role(UserRole role) { this.role = role; return this; }
        public LoginResponse build() { return new LoginResponse(userId, username, role); }
    }
}
