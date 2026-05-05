package com.internship.tool.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secret = "test-secret-key-for-testing-purposes-only";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        java.lang.reflect.Field field;
        try {
            field = jwtUtil.getClass().getDeclaredField("secret");
            field.setAccessible(true);
            field.set(jwtUtil, secret);
        } catch (Exception e) {
            fail("Failed to set secret field");
        }
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testuser", "USER");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testuser", "USER");
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testuser", "USER");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateInvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }
}
