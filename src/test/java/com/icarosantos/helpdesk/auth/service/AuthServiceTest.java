package com.icarosantos.helpdesk.auth.service;

import com.icarosantos.helpdesk.auth.dto.LoginRequest;
import com.icarosantos.helpdesk.common.exception.InvalidCredentialsException;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.domain.UserRole;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService service;

    @Test
    void should_authenticate_with_valid_credentials() {
        var existingUser = User.builder()
                .id(UUID.randomUUID())
                .email("client@example.com")
                .password("hashedPassword")
                .role(UserRole.CLIENT)
                .build();

        var request = new LoginRequest("client@example.com", "plainPassword");

        when(userRepository.findByEmail("client@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        var result = service.authenticate(request);

        assertThat(result.getEmail()).isEqualTo("client@example.com");
    }

    @Test
    void should_reject_invalid_password() {
        var existingUser = User.builder()
                .id(UUID.randomUUID())
                .email("client@example.com")
                .password("hashedPassword")
                .role(UserRole.CLIENT)
                .build();

        var request = new LoginRequest("client@example.com", "wrongPassword");

        when(userRepository.findByEmail("client@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThatThrownBy(() -> service.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void should_reject_non_existing_user() {
        var request = new LoginRequest("unknown@example.com", "anyPassword");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}