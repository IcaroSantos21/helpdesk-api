package com.icarosantos.helpdesk.user.service;

import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.domain.UserRole;
import com.icarosantos.helpdesk.user.dto.RegisterUserRequest;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public User register(RegisterUserRequest request) {

        repository.existsByEmail(request.email());

        var passwordHash = passwordEncoder.encode(request.password());
        var user = User.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .password(passwordHash)
                .role(request.role())
                .build();
        return repository.save(user);
    }
}
