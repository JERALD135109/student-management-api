package com.jerald.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Token is valid for 1 hour. After that the user must log in again.
    private static final long EXPIRY_MS = 60L * 60 * 1000;

    private final SecretKey key;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        // Secret must be at least 32 bytes long for HS256
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRY_MS))
                .signWith(key)
                .compact();
    }

    // Throws JwtException if the token is forged, tampered, or expired
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
