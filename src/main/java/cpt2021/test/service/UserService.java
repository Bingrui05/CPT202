package cpt2021.test.service;

import cpt2021.test.dto.RegisterRequest;
import cpt2021.test.entity.User;
import cpt2021.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER");  // 默认客户
        return userRepository.save(user);
    }

    public User findByUsernameOrEmail(String login) {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElse(null);
    }
}