package com.icarosantos.helpdesk.ticket.service;

import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import com.icarosantos.helpdesk.ticket.dto.CreateTicketRequest;
import com.icarosantos.helpdesk.ticket.repository.TicketRepository;
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

        if (request.description() == null || request.description().isBlank())
            throw new IllegalArgumentException("description must not be blank");

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

    private void validateTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title must not be blank");

        if (title.length() < 5)
            throw new IllegalArgumentException("title must contain at least 5 characters");

        if (title.length() > 100)
            throw new IllegalArgumentException("title must contain at most 100 characters");
    }
}
