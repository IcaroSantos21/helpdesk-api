package com.icarosantos.helpdesk.ticket.dto;

import com.icarosantos.helpdesk.ticket.domain.TicketPriority;
import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(
        @NotBlank
        String title,
        String description,
        TicketPriority priority
) {
}
