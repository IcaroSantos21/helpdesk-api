package com.icarosantos.helpdesk.ticket.dto;

import com.icarosantos.helpdesk.ticket.domain.TicketPriority;

public record CreateTicketRequest(
        String title,
        String description,
        TicketPriority priority
) {
}
