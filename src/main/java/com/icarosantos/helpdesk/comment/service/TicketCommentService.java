package com.icarosantos.helpdesk.comment.service;

import com.icarosantos.helpdesk.comment.domain.TicketComment;
import com.icarosantos.helpdesk.comment.dto.AddCommentRequest;
import com.icarosantos.helpdesk.comment.repository.TicketCommentRepository;
import com.icarosantos.helpdesk.common.exception.InvalidCommentException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class TicketCommentService {

    private final TicketCommentRepository repository;


    public TicketComment addComment(UUID ticketId, AddCommentRequest request) {

        if (request.content() == null || request.content().isBlank())
            throw new InvalidCommentException("Comment content must not be blank");

        if (request.content().length() > 1000)
            throw new InvalidCommentException("Comment content must not exceed 1000 characters");

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
