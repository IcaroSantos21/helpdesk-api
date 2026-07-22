package com.icarosantos.helpdesk.common.exception;

import com.icarosantos.helpdesk.user.domain.UserRole;

public class UnauthorizedAssignmentException extends RuntimeException {
    public UnauthorizedAssignmentException(String message) {
        super(message);
    }
}
