package org.example.garageservicebackend;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.garageservicebackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ExpiredJwtTokenTest {

    @Test
    void shouldRejectExpiredToken() {

        // Generate a secure test key
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);

        String testSecret = Base64.getEncoder()
                .encodeToString(keyBytes);

        // Configure JwtService with the test key
        JwtService jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService, "jwtSecret", testSecret
        );

        SecretKey signingKey = Keys.hmacShaKeyFor(keyBytes);

        long now = System.currentTimeMillis();

        // Create an already-expired JWT
        String expiredToken = Jwts.builder()
                .subject("manager@example.com")
                .claim("managerId", UUID.randomUUID().toString())
                .claim("role", "MANAGER")
                .issuedAt(new Date(now - 7200000L))
                .expiration(new Date(now - 3600000L))
                .signWith(signingKey)
                .compact();

        // Verify that JwtService rejects it
        assertFalse(jwtService.validateToken(expiredToken));
    }
}