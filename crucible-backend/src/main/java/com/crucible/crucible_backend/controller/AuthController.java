package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.entity.User;
import com.crucible.crucible_backend.repository.UserRepository;
import com.crucible.crucible_backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already in use"));
        }

        User user = new User();
        user.setEmail(email);
        // Hashes the password via BCrypt before saving to Postgres
        user.setPasswordHash(passwordEncoder.encode(password));

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String rawPassword = request.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);

        // Validates identity: Rejects if user doesn't exist or if BCrypt hash check fails
        if (userOpt.isEmpty() || !passwordEncoder.matches(rawPassword, userOpt.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        // Only issues the JWT using the authenticated user's actual database ID
        String userId = userOpt.get().getId().toString();
        String token = jwtService.generateToken(userId);

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "token", token
        ));
    }
}