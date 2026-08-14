package com.icarosantos.helpdesk.auth.dto;

public record LoginResponse(String token, String email, String role) {
}
