package com.cpt202.consultationbooking.entity;

import com.cpt202.consultationbooking.enums.UserRole;
import com.cpt202.consultationbooking.enums.UserStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public User() {}

    public User(Long userId, String username, String password, String email, String address, UserRole role, UserStatus status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private Long userId;
        private String username;
        private String password;
        private String email;
        private String address;
        private UserRole role;
        private UserStatus status;

        public UserBuilder userId(Long userId) { this.userId = userId; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder address(String address) { this.address = address; return this; }
        public UserBuilder role(UserRole role) { this.role = role; return this; }
        public UserBuilder status(UserStatus status) { this.status = status; return this; }
        public User build() { return new User(userId, username, password, email, address, role, status); }
    }
}
