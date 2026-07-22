package com.icarosantos.helpdesk.ticket.service;

import com.icarosantos.helpdesk.common.exception.UnauthorizedAssignmentException;
import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketPriority;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import com.icarosantos.helpdesk.ticket.dto.CreateTicketRequest;
import com.icarosantos.helpdesk.ticket.repository.TicketRepository;
import com.icarosantos.helpdesk.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @Test
    void should_set_creation_date() {
        // Arrange
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
        assertThat(created.getCreatedAt()).isNotNull();
        assertThat(created.getUpdatedAt()).isNotNull();
        assertThat(created.getCreatedAt()).isEqualTo(created.getUpdatedAt());
    }

    @Test
    void should_reject_blank_title() {
        UUID clientId = UUID.randomUUID();

        var request = new CreateTicketRequest(
                "   ",
                "Descrição válida com mais de dez caracteres",
                TicketPriority.HIGH
        );

        assertThatThrownBy(() -> service.create(request, clientId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title must not be blank");
    }

    @Test
    void should_reject_title_shorter_than_5() {
        UUID clientId = UUID.randomUUID();

        var request = new CreateTicketRequest(
                "Erro",
                "Descrição válida com mais de dez caracteres",
                TicketPriority.HIGH
        );

        assertThatThrownBy(() -> service.create(request, clientId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title must contain at least 5 characters");
    }

    @Test
    void should_reject_title_longer_than_100() {
        UUID clientId = UUID.randomUUID();

        String longTitle = "a".repeat(101);

        var request = new CreateTicketRequest(
                longTitle,
                "Descrição válida com mais de dez caracteres",
                TicketPriority.HIGH
        );

        assertThatThrownBy(() -> service.create(request, clientId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title must contain at most 100 characters");
    }

    @Test
    void should_reject_blank_description() {
        UUID clientId = UUID.randomUUID();

        var request = new CreateTicketRequest(
                "Erro no login",
                "   ",
                TicketPriority.HIGH
        );

        assertThatThrownBy(() -> service.create(request, clientId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("description must not be blank");
    }

    @Test
    void should_reject_description_shorter_than_10() {
        UUID clientId = UUID.randomUUID();

        var request = new CreateTicketRequest(
                "Erro no login",
                "NãoTemDez",
                TicketPriority.HIGH
        );

        assertThatThrownBy(() -> service.create(request, clientId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("description must contain at least 10 characters");
    }

    @Test
    void should_assign_ticket_to_agent() {
        var ticketId = UUID.randomUUID();
        var agentId = UUID.randomUUID();

        var existingTicket = Ticket.builder()
                .id(ticketId)
                .title("Login not working")
                .description("User cannot login to the platform")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.HIGH)
                .createdBy(UUID.randomUUID())
                .assignedTo(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.assign(ticketId, agentId, UserRole.AGENT);

        assertThat(result.getAssignedTo()).isEqualTo(agentId);
        assertThat(result.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void should_reject_assignment_by_client() {
        var ticketId = UUID.randomUUID();
        var agentId = UUID.randomUUID();

        var existingTicket = Ticket.builder()
                .id(ticketId)
                .title("Login not working")
                .description("User cannot login to the platform")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.HIGH)
                .createdBy(UUID.randomUUID())
                .assignedTo(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.findById(ticketId)).thenReturn(Optional.of(existingTicket));

        assertThatThrownBy(() -> service.assign(ticketId, agentId, UserRole.CLIENT))
                .isInstanceOf(UnauthorizedAssignmentException.class);
    }
}