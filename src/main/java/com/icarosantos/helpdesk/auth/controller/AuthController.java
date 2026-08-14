package com.icarosantos.helpdesk.auth.controller;

import com.icarosantos.helpdesk.auth.dto.LoginRequest;
import com.icarosantos.helpdesk.auth.dto.LoginResponse;
import com.icarosantos.helpdesk.auth.security.JwtService;
import com.icarosantos.helpdesk.auth.service.AuthService;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.dto.RegisterUserRequest;
import com.icarosantos.helpdesk.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, UserService userService, JwtService jwtService) {
        this.authService = authService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        var user = authService.authenticate(request);
        var token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token, user.getEmail(), user.getRole().name());
    }

    @PostMapping("/auth/register")
    public User register(@RequestBody RegisterUserRequest request) {
        return userService.register(request);
    }
}
