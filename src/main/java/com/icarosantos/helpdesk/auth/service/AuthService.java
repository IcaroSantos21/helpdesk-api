package com.icarosantos.helpdesk.auth.service;

import com.icarosantos.helpdesk.auth.dto.LoginRequest;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User authenticate(LoginRequest request) {
        return null;
    }
}
