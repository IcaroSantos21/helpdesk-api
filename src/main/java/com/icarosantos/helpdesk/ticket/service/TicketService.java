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
