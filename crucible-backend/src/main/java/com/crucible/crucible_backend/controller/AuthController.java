package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Endpoint: POST /api/auth/login
     * Issues a stateless JWT for a given user ID.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String userId) {
        // In a production environment, you would query your Postgres Database
        // and hash-check the password here before issuing the token.
        // For testing the vertical slice, we generate the token directly.
        String token = jwtService.generateToken(userId);

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "token", token
        ));
    }
}