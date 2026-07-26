package com.icarosantos.helpdesk.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService service;

    @BeforeEach
    void setUp() {
        var properties = new JwtProperties("my-super-secret-test-key-for-jwt-tests", 86400000L);
        service = new JwtService(properties);
    }

    @Test
    void should_generate_jwt_token() {
        var token = service.generateToken("client@example.com");

        assertThat(token).isNotNull();
    }

    @Test
    void should_extract_username_from_token() {
        var token = service.generateToken("client@example.com");

        var extractedEmail = service.extractUsername(token);

        assertThat(extractedEmail).isEqualTo("client@example.com");
    }

    @Test
    void should_validate_valid_token() {
        var token = service.generateToken("client@example.com");

        var isValid = service.isTokenValid(token);

        assertThat(isValid).isTrue();
    }
}