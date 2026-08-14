package com.icarosantos.helpdesk.user.service;

import com.icarosantos.helpdesk.common.exception.DuplicateEmailException;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.domain.UserRole;
import com.icarosantos.helpdesk.user.dto.RegisterUserRequest;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @Test
    void should_register_client_user() {
        var request = new RegisterUserRequest("client", "client@example.com", "plainPassword", UserRole.CLIENT);

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(repository.existsByEmail("client@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.register(request);

        assertThat(result.getEmail()).isEqualTo("client@example.com");
        assertThat(result.getRole()).isEqualTo(UserRole.CLIENT);
        assertThat(result.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    void should_register_agent_user() {
        var request = new RegisterUserRequest("agent", "agent@example.com", "plainPassword", UserRole.AGENT);

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(repository.existsByEmail("agent@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.register(request);

        assertThat(result.getEmail()).isEqualTo("agent@example.com");
        assertThat(result.getRole()).isEqualTo(UserRole.AGENT);
        assertThat(result.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    void should_reject_duplicate_email() {
        var request = new RegisterUserRequest("client", "client@example.com", "plainPassword", UserRole.CLIENT);

        when(repository.existsByEmail("client@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void should_hash_password_before_save() {
        var request = new RegisterUserRequest("client", "client@example.com", "plainPassword", UserRole.CLIENT);

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(repository.existsByEmail("client@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.register(request);

        assertThat(result.getPassword()).isEqualTo("hashedPassword");
        assertThat(result.getPassword()).isNotEqualTo("plainPassword");
        verify(passwordEncoder).encode("plainPassword");
    }

    @Test
    void should_set_default_role_as_client_when_not_provided() {
        var request = new RegisterUserRequest("client", "client@example.com", "plainPassword", null);

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(repository.existsByEmail("client@example.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.register(request);

        assertThat(result.getRole()).isEqualTo(UserRole.CLIENT);
    }
}