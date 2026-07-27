package com.icarosantos.helpdesk.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_allow_access_to_auth_endpoints() throws Exception {
        var loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andReturn();

        var registerResult = mockMvc.perform(post("auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andReturn();

        assertThat(loginResult.getResponse().getStatus()).isNotIn(401, 403);
        assertThat(registerResult.getResponse().getStatus()).isNotIn(401, 403);
    }
}
