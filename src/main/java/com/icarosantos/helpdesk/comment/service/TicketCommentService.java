package com.icarosantos.helpdesk.comment.service;

import com.icarosantos.helpdesk.comment.domain.TicketComment;
import com.icarosantos.helpdesk.comment.dto.AddCommentRequest;
import com.icarosantos.helpdesk.comment.repository.TicketCommentRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class TicketCommentService {

    private final TicketCommentRepository repository;


    public TicketComment addComment(UUID ticketId, AddCommentRequest request) {

        var ticketComment = TicketComment.builder()
                .id(UUID.randomUUID())
                .ticketId(ticketId)
                .authorId(request.authorId())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .build();

        return repository.save(ticketComment);
    }
}
