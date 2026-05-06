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
    private final OperationManagerRepository operationManagerRepository;
    private final SpecialistRepository specialistRepository;

    public AuthService(UserRepository userRepository,
                       CustomerRepository customerRepository,
                       OperationManagerRepository operationManagerRepository,
                       SpecialistRepository specialistRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.operationManagerRepository = operationManagerRepository;
        this.specialistRepository = specialistRepository;
    }

    @Transactional
    public User register(RegisterRequest request) {
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
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Invalid username or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new BusinessException("Account is inactive");
        }

        Long customerId = user.getRole() == UserRole.CUSTOMER
                ? customerRepository.findByUser_UserId(user.getUserId()).map(Customer::getCustomerId).orElse(null)
                : null;
        Long specialistId = user.getRole() == UserRole.SPECIALIST
                ? specialistRepository.findByUser_UserId(user.getUserId()).map(Specialist::getSpecialistId).orElse(null)
                : null;
        Long managerId = user.getRole() == UserRole.MANAGER
                ? operationManagerRepository.findByUser_UserId(user.getUserId()).map(OperationManager::getManagerId).orElse(null)
                : null;

        return LoginResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .customerId(customerId)
                .specialistId(specialistId)
                .managerId(managerId)
                .build();
    }
}
