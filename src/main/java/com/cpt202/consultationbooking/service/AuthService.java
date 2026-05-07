package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.LoginRequest;
import com.cpt202.consultationbooking.dto.request.RegisterRequest;
import com.cpt202.consultationbooking.dto.response.LoginResponse;
import com.cpt202.consultationbooking.entity.Customer;
import com.cpt202.consultationbooking.entity.OperationManager;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.enums.UserRole;
import com.cpt202.consultationbooking.enums.UserStatus;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.repository.CustomerRepository;
import com.cpt202.consultationbooking.repository.OperationManagerRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import com.cpt202.consultationbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final OperationManagerRepository operationManagerRepository;

    public AuthService(UserRepository userRepository,
                      CustomerRepository customerRepository,
                      SpecialistRepository specialistRepository,
                      OperationManagerRepository operationManagerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.specialistRepository = specialistRepository;
        this.operationManagerRepository = operationManagerRepository;
    }

    @Transactional
    public User register(RegisterRequest request) {
        // Validate registration input
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!request.getEmail().contains("@") || !request.getEmail().contains(".")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (request.getRole() == null) {
            throw new IllegalArgumentException("Role is required");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .address(request.getAddress())
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        if (request.getRole() == UserRole.CUSTOMER) {
            Customer customer = Customer.builder()
                    .user(savedUser)
                    .build();
            customerRepository.save(customer);
        } else if (request.getRole() == UserRole.MANAGER) {
            OperationManager manager = OperationManager.builder()
                    .user(savedUser)
                    .build();
            operationManagerRepository.save(manager);
        }

        return savedUser;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Invalid username or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new BusinessException("Account is inactive");
        }

        Long customerId = null;
        Long specialistId = null;
        Long managerId = null;

        if (user.getRole() == UserRole.CUSTOMER) {
            Customer customer = customerRepository.findByUser_UserId(user.getUserId()).orElse(null);
            if (customer != null) {
                customerId = customer.getCustomerId();
            }
        } else if (user.getRole() == UserRole.SPECIALIST) {
            Specialist specialist = specialistRepository.findByUser_UserId(user.getUserId()).orElse(null);
            if (specialist != null) {
                specialistId = specialist.getSpecialistId();
            }
        } else if (user.getRole() == UserRole.MANAGER) {
            OperationManager manager = operationManagerRepository.findByUser_UserId(user.getUserId()).orElse(null);
            if (manager != null) {
                managerId = manager.getManagerId();
            }
        }

        return LoginResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .customerId(customerId)
                .specialistId(specialistId)
                .managerId(managerId)
                .build();
    }
}
