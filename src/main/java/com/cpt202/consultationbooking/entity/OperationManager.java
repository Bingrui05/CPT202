package com.cpt202.consultationbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_managers")
public class OperationManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public OperationManager() {}

    public OperationManager(Long managerId, User user) {
        this.managerId = managerId;
        this.user = user;
    }

    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static OperationManagerBuilder builder() { return new OperationManagerBuilder(); }

    public static class OperationManagerBuilder {
        private Long managerId;
        private User user;

        public OperationManagerBuilder managerId(Long managerId) { this.managerId = managerId; return this; }
        public OperationManagerBuilder user(User user) { this.user = user; return this; }
        public OperationManager build() { return new OperationManager(managerId, user); }
    }
}
