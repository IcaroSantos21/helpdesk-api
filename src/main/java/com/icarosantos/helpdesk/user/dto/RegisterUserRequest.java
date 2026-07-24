package com.icarosantos.helpdesk.user.dto;

import com.icarosantos.helpdesk.user.domain.UserRole;

public record RegisterUserRequest(
        String email,
        String password,
        UserRole role
) {
}
