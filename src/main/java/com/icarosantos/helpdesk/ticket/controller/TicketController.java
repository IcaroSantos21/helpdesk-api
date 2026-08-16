package com.icarosantos.helpdesk.ticket.controller;

import com.icarosantos.helpdesk.ticket.dto.AssignTicketRequest;
import com.icarosantos.helpdesk.ticket.dto.ChangeStatusRequest;
import com.icarosantos.helpdesk.ticket.dto.CreateTicketRequest;
import com.icarosantos.helpdesk.ticket.dto.TicketResponse;
import com.icarosantos.helpdesk.ticket.service.TicketService;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody @Valid CreateTicketRequest request, Authentication authentication) {
        var clientId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        var ticket = ticketService.create(request, clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.from(ticket));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable UUID id, @RequestBody AssignTicketRequest request, Authentication authentication) {
        var userRole = userRepository.findByEmail(authentication.getName()).orElseThrow().getRole();
        var ticket = ticketService.assign(id, request.agentId(), userRole);
        return ResponseEntity.status(HttpStatus.OK).body(TicketResponse.from(ticket));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> changeStatus(@PathVariable UUID id, @RequestBody ChangeStatusRequest request, Authentication authentication) {
        var userRole = userRepository.findByEmail(authentication.getName()).orElseThrow().getRole();
        var ticket = ticketService.changeStatus(id, request.status(), userRole);
        return ResponseEntity.status(HttpStatus.OK).body(TicketResponse.from(ticket));
    }
}
