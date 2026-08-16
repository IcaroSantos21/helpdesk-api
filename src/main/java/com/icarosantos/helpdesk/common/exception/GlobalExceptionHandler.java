package com.icarosantos.helpdesk.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAssignmentException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAssignment(UnauthorizedAssignmentException ex) {
        return errorResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage());
    }

    @ExceptionHandler(TicketAlreadyAssignedException.class)
    public ResponseEntity<Map<String, String>> handleTicketAlreadyAssigned(TicketAlreadyAssignedException ex) {
        return errorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTicketNotFound(TicketNotFoundException ex) {
        return errorResponse(HttpStatus.NOT_FOUND, "Not_found", ex.getMessage());
    }

    private ResponseEntity<Map<String, String>> errorResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(Map.of("error", error, "message", message));
    }

}
