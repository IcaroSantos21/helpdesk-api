package com.icarosantos.helpdesk.auth.security;

public record JwtProperties(String secret, Long expiration) {
}
