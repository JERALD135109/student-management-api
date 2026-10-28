package com.jerald.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final String adminUser;
    private final String adminPassword;

    public AuthController(JwtService jwtService,
                          @Value("${spring.security.user.name}") String adminUser,
                          @Value("${spring.security.user.password}") String adminPassword) {
        this.jwtService = jwtService;
        this.adminUser = adminUser;
        this.adminPassword = adminPassword;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");

        if (matches(adminUser, username) && matches(adminPassword, password)) {
            return ResponseEntity.ok(Map.of("token", jwtService.generateToken(username)));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
    }

    // Compares in constant time, so the check does not leak how many characters matched
    private boolean matches(String expected, String actual) {
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                actual.getBytes(StandardCharsets.UTF_8));
    }
}
