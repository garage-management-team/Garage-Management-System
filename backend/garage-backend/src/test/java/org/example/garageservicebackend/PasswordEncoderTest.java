package org.example.garageservicebackend;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    void shouldEncodeAndVerifyPassword() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "Manager@123";

        // Encode password
        String hashedPassword = encoder.encode(password);

        // Verify correct password
        assertTrue(encoder.matches(password, hashedPassword));

        // Verify incorrect password
        assertFalse(encoder.matches("WrongPassword", hashedPassword));
    }
}