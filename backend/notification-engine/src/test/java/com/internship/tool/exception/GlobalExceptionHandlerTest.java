package com.internship.tool.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @Test
    void testErrorResponseCreation() {
        ErrorResponse error = new ErrorResponse(404, "Not Found", java.time.LocalDateTime.now());
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getMessage());
        assertNotNull(error.getTimestamp());
    }

    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionWithId() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", 1L);
        assertTrue(ex.getMessage().contains("User"));
        assertTrue(ex.getMessage().contains("1"));
    }

    @Test
    void testUserAlreadyExistsException() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User already exists");
        assertEquals("User already exists", ex.getMessage());
    }

    @Test
    void testInvalidCredentialsException() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void testValidationException() {
        ValidationException ex = new ValidationException("Validation failed");
        assertEquals("Validation failed", ex.getMessage());
    }
}
