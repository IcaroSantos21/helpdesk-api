package com.icarosantos.helpdesk.auth.controller;

import com.icarosantos.helpdesk.auth.dto.LoginRequest;
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

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/auth/register")
    public User register(@RequestBody RegisterUserRequest request) {
        return userService.register(request);
    }
}
