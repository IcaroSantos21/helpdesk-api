package com.icarosantos.helpdesk.user.dto;

import com.icarosantos.helpdesk.user.domain.UserRole;

public record RegisterUserRequest(
        String username,
        String email,
        String password,
        UserRole role
) {
}
