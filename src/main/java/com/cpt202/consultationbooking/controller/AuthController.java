package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.LoginRequest;
import com.cpt202.consultationbooking.dto.request.RegisterRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.LoginResponse;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
