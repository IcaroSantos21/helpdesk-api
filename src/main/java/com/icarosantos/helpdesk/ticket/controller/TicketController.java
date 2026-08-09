package com.icarosantos.helpdesk.ticket.controller;

import com.icarosantos.helpdesk.ticket.dto.CreateTicketRequest;
import com.icarosantos.helpdesk.ticket.dto.TicketResponse;
import com.icarosantos.helpdesk.ticket.service.TicketService;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    @PostMapping("/tickets")
    public ResponseEntity<TicketResponse> create(@RequestBody CreateTicketRequest request , Authentication authentication) {

        var client = userRepository.findByEmail(authentication.getName()).orElseThrow();
        var ticket = ticketService.create(request, client.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.from(ticket));

    }
}
