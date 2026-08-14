package com.icarosantos.helpdesk.ticket.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AssignTicketRequest(@NotBlank UUID agentId) {
}
