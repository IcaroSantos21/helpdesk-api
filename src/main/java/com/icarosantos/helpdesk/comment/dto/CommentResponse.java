package com.icarosantos.helpdesk.comment.dto;

import com.icarosantos.helpdesk.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(UUID id, UUID ticketId, UUID authorId, String content, LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTicketId(),
                comment.getAuthorId(),
                comment.getMessage(),
                comment.getCreatedAt()
        );
    }
}
