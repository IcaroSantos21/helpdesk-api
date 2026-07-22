package com.icarosantos.helpdesk.ticket.service;

import com.icarosantos.helpdesk.common.exception.UnauthorizedAssignmentException;
import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import com.icarosantos.helpdesk.ticket.dto.CreateTicketRequest;
import com.icarosantos.helpdesk.ticket.repository.TicketRepository;
import com.icarosantos.helpdesk.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;

    public Ticket create(CreateTicketRequest request, UUID clientId) {
        var now = LocalDateTime.now();

        validateTitle(request.title());

        validateDescription(request.description());

        var ticket = Ticket.builder()
                .title(request.title())
                .description(request.description())
                .status(TicketStatus.OPEN)
                .priority(request.priority())
                .createdBy(clientId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return repository.save(ticket);

    }

    public Ticket assign(UUID ticketId, UUID agentId, UserRole requesterRole) {
        var ticket = repository.findById(ticketId).get();

        if (requesterRole == UserRole.CLIENT) {
            throw new UnauthorizedAssignmentException("Clients are not allowed to assign tickets");
        }


        ticket.setAssignedTo(agentId);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setUpdatedAt(LocalDateTime.now());

        return repository.save(ticket);
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title must not be blank");

        if (title.length() < 5)
            throw new IllegalArgumentException("title must contain at least 5 characters");

        if (title.length() > 100)
            throw new IllegalArgumentException("title must contain at most 100 characters");
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("description must not be blank");

        if (description.length() < 10)
            throw new IllegalArgumentException("description must contain at least 10 characters");
    }
}
