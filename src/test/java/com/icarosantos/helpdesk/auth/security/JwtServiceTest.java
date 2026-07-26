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

    @Test
    void should_reject_expired_token() {
        var expiredProperties = new JwtProperties("test-secret-key-test-secret-key-1234567890", -1000L);
        var expiredJwtService = new JwtService(expiredProperties);
        var expiredToken = expiredJwtService.generateToken("client@example.com");

        var isValid = service.isTokenValid(expiredToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void should_reject_invalid_token() {
        var invalidToken = "this.is.not-a-valid-token";

        var isValid = service.isTokenValid(invalidToken);

        assertThat(isValid).isFalse();
    }
}