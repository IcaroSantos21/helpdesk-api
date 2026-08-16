package com.icarosantos.helpdesk.ticket.dto;

import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import jakarta.validation.constraints.NotBlank;

public record ChangeStatusRequest(@NotBlank TicketStatus status) {
}
