package com.icarosantos.helpdesk.common.exception;

public class TicketAlreadyAssignedException extends RuntimeException {
    public TicketAlreadyAssignedException(String message) {
        super(message);
    }
}
