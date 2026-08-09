package com.icarosantos.helpdesk.ticket.dto;

import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketPriority;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public  record TicketResponse(
        UUID id,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        UUID createdBy,
        UUID assignedTo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedBy(),
                ticket.getAssignedTo(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
