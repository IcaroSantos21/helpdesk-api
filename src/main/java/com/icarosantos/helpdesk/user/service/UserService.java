package com.icarosantos.helpdesk.user.service;

import com.icarosantos.helpdesk.common.exception.DuplicateEmailException;
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

        if (repository.existsByEmail(request.email()))
            throw new DuplicateEmailException("Email is already in use");

        var passwordHash = passwordEncoder.encode(request.password());
        var user = User.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .password(passwordHash)
                .role(request.role() == null ? UserRole.CLIENT : request.role())
                .build();
        return repository.save(user);
    }
}
