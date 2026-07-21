package com.icarosantos.helpdesk.ticket.repository;

import com.icarosantos.helpdesk.ticket.domain.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<Ticket, UUID> {
}
