package com.icarosantos.helpdesk.ticket.controller;

import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketPriority;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import com.icarosantos.helpdesk.ticket.repository.TicketRepository;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.domain.UserRole;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private User agent;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        var client = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .username("client")
                .email("client@helpdesk")
                .password("irrelevant-for-this-thes")
                .role(UserRole.CLIENT)
                .build());

        agent = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .username("agent")
                .email("agent@helpdesk")
                .password("irrelevant-for-this-thes")
                .role(UserRole.AGENT)
                .build());

        ticket = ticketRepository.save(Ticket.builder()
                .title("Erro no login")
                .description("Não consigo acessar o sistema")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.HIGH)
                .createdBy(client.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Test
    @WithMockUser(username = "client@helpdesk", roles = "CLIENT")
    void should_create_ticket_via_http() throws Exception {
        var request = """
                {
                    "title": "Login error",
                    "description": "I cannot access the system.",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Login error"))
                .andExpect(jsonPath("$.description").value("I cannot access the system."))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    @WithMockUser(username = "client@helpdesk", roles = "CLIENT")
    void should_return_201_when_ticket_is_created() throws Exception {
        var request = """
            {
                "title": "Login error",
                "description": "I cannot access the system.",
                "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "client@helpdesk", roles = "CLIENT")
    void should_return_400_for_invalid_title() throws Exception {
        var request = """
            {
                "title": "",
                "description": "I cannot access the system.",
                "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_401_when_request_has_no_token() throws Exception {
        var request = """
            {
                "title": "Erro no login",
                "description": "Não consigo acessar o sistema",
                "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "agent@helpdesk", roles = "AGENT")
    void should_assign_ticket_via_http() throws Exception {
        var request = """
                {
                    "agentId": "%s"
                }
                """.formatted(agent.getId());

        mockMvc.perform(patch("/tickets/{id}/assign", ticket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.assignedTo").value(agent.getId().toString()));
    }
}
