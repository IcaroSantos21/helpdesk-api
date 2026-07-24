package com.icarosantos.helpdesk.comment.repository;

import com.icarosantos.helpdesk.comment.domain.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketCommentRepository extends JpaRepository<TicketComment, UUID> {
}
