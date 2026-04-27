package com.internship.tool.service;

import com.internship.tool.dto.AuthResponse;
import com.internship.tool.dto.LoginRequest;
import com.internship.tool.dto.RegisterRequest;
import com.internship.tool.entity.User;
import com.internship.tool.exception.InvalidCredentialsException;
import com.internship.tool.exception.UserAlreadyExistsException;
import com.internship.tool.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userService.getUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            return new AuthResponse(token, user.getUsername(), user.getRole());
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        if (userService.userExists(request.getUsername(), request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        user.setRole("USER");
        User savedUser = userService.createUser(user);

        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole());

        return new AuthResponse(token, savedUser.getUsername(), savedUser.getRole());
    }

    public AuthResponse refreshToken(String token) {
        String username = jwtUtil.extractUsername(token);
        if (username != null && jwtUtil.validateToken(token)) {
            User user = userService.getUserByUsername(username);
            String newToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
            return new AuthResponse(newToken, user.getUsername(), user.getRole());
        }
        throw new InvalidCredentialsException("Invalid token");
    }
}
