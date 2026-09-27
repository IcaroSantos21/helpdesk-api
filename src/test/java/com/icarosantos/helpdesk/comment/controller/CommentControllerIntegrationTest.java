package com.icarosantos.helpdesk.comment.controller;

import com.icarosantos.helpdesk.ticket.domain.Ticket;
import com.icarosantos.helpdesk.ticket.domain.TicketPriority;
import com.icarosantos.helpdesk.ticket.domain.TicketStatus;
import com.icarosantos.helpdesk.ticket.repository.TicketRepository;
import com.icarosantos.helpdesk.user.domain.User;
import com.icarosantos.helpdesk.user.domain.UserRole;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private User client;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        client = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .username("client")
                .email("client@helpdesk")
                .password("irrelevant-for-this-test")
                .role(UserRole.CLIENT)
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
    void should_add_comment_via_http() throws Exception {
        var request = """
                {
                    "content": "Estou com o mesmo problema"
                }
                """;

        mockMvc.perform(post("/tickets/{id}/comments", ticket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Estou com o mesmo problema"))
                .andExpect(jsonPath("$.authorId").value(client.getId().toString()));
    }
}
