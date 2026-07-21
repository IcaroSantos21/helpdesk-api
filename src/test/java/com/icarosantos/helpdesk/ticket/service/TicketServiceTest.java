package com.icarosantos.helpdesk.ticket.service;

import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketPriority;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import com.icarosantos.helpdesk.ticket.dto.CreateTicketRequest;
import com.icarosantos.helpdesk.ticket.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    @Test
    void should_create_ticket_with_open_status() {

        UUID clientId = UUID.randomUUID();

        CreateTicketRequest request = new CreateTicketRequest(
                "Erro no login",
                "Não consigo acessar o sistema",
                TicketPriority.HIGH
        );

        when(repository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket created = service.create(request, clientId);

        // Assert
        assertThat(created).isNotNull();
        assertThat(created.getStatus()).isEqualTo(TicketStatus.OPEN);
        assertThat(created.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(created.getCreatedBy()).isEqualTo(clientId);
        assertThat(created.getAssignedTo()).isNull();

    }
}