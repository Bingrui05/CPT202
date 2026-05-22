package com.cpt202.consultationbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Customer() {}

    public Customer(Long customerId, User user) {
        this.customerId = customerId;
        this.user = user;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static CustomerBuilder builder() { return new CustomerBuilder(); }

    public static class CustomerBuilder {
        private Long customerId;
        private User user;

        public CustomerBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
        public CustomerBuilder user(User user) { this.user = user; return this; }
        public Customer build() { return new Customer(customerId, user); }
    }
}
