package com.icarosantos.helpdesk.auth.service;

import com.icarosantos.helpdesk.auth.dto.LoginRequest;
import com.icarosantos.helpdesk.common.exception.InvalidCredentialsException;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(LoginRequest request) {
        var user = userRepository.findByEmail(request.email()).get();
        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid email or password");
        return user;
    }
}
