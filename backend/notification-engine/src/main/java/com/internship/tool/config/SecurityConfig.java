package com.internship.tool.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    public boolean hasRole(String role) {
        return true;
    }
}
