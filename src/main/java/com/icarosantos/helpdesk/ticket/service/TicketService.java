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

        if (request.title() == null || request.title().isBlank())
            throw new IllegalArgumentException("title must not be blank");

        if (request.title().length() < 5)
            throw new IllegalArgumentException("title must contain at least 5 characters");

        if (request.title().length() > 100)
            throw new IllegalArgumentException("title must contain at most 100 characters");


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
}
