package com.icarosantos.helpdesk.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.icarosantos.helpdesk.auth.service.AuthService;
import com.icarosantos.helpdesk.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @Test
    void should_allow_access_to_auth_endpoints() throws Exception {
        var loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        var registerResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        assertThat(loginResult.getResponse().getStatus()).isNotIn(401, 403);
        assertThat(registerResult.getResponse().getStatus()).isNotIn(401, 403);
    }

    @Test
    void should_require_authentication_for_protected_endpoints() throws Exception {
        var result = mockMvc.perform(get("/tickets"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isIn(401, 403);
    }

    @Test
    void should_authenticate_request_with_valid_token() throws Exception {
        var email = "client@example.com";
        var token = jwtService.generateToken(email);

        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("irrelevant")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        var result = mockMvc.perform(get("/tickets")
                        .header("Authorization", "Bearer " + token))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isNotIn(401, 403);
    }

    @Test
    void should_reject_request_with_invalid_token() throws Exception {
        var result = mockMvc.perform(get("/tickets")
                        .header("Authorization", "Bearer this.is.not-a-valid-token"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isIn(401, 403);
    }
}