package com.icarosantos.helpdesk.ticket.controller;

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

import java.util.UUID;

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

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .username("client")
                .email("client@helpdesk")
                .password("irrelevant-for-this-thes")
                .role(UserRole.CLIENT)
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
}
