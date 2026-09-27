package com.icarosantos.helpdesk.comment.service;

import com.icarosantos.helpdesk.comment.domain.Comment;
import com.icarosantos.helpdesk.comment.dto.AddCommentRequest;
import com.icarosantos.helpdesk.comment.repository.CommentRepository;
import com.icarosantos.helpdesk.common.exception.InvalidCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;


    public Comment addComment(UUID ticketId, AddCommentRequest request, UUID authorId) {

        validateContent(request);

        var ticketComment = Comment.builder()
                .id(UUID.randomUUID())
                .ticketId(ticketId)
                .authorId(authorId)
                .message(request.content())
                .createdAt(LocalDateTime.now())
                .build();

        return repository.save(ticketComment);
    }

    private static void validateContent(AddCommentRequest request) {
        if (request.content() == null || request.content().isBlank())
            throw new InvalidCommentException("Comment content must not be blank");

        if (request.content().length() > 1000)
            throw new InvalidCommentException("Comment content must not exceed 1000 characters");
    }
}
