package com.icarosantos.helpdesk.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Date;

public class JwtService {

    private final JwtProperties properties;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
    }

    public String generateToken(String email) {
        var secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes());
        var now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(now))
                .expiration(new Date(now + properties.expiration()))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        var secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes());
        var payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        var subject = payload.getSubject();

        return subject;
    }
}
