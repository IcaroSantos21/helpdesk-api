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
        var body = Map.of("error", "Forbidden", "message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(TicketAlreadyAssignedException.class)
    public ResponseEntity<Map<String, String>> handleTicketAlreadyAssigned(TicketAlreadyAssignedException ex) {
        var body = Map.of("error", "Conflict", "message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
