package com.icarosantos.helpdesk.comment.service;

import com.icarosantos.helpdesk.comment.domain.Comment;
import com.icarosantos.helpdesk.comment.dto.AddCommentRequest;
import com.icarosantos.helpdesk.comment.repository.CommentRepository;
import com.icarosantos.helpdesk.common.exception.InvalidCommentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;


import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketCommentServiceTest {

    @Mock
    private CommentRepository repository;

    @InjectMocks
    private CommentService service;

    @Test
    void should_add_comment() {
        var ticketId = UUID.randomUUID();
        var authorId = UUID.randomUUID();
        var request = new AddCommentRequest("This issue is still happening");

        when(repository.save(any(Comment.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        var result = service.addComment(ticketId, request, authorId);

        assertThat(result.getTicketId()).isEqualTo(ticketId);
        assertThat(result.getMessage()).isEqualTo("This issue is still happening");
    }

    @Test
    void should_reject_blank_comment() {
        var ticketId = UUID.randomUUID();
        var authorId = UUID.randomUUID();
        var request = new AddCommentRequest("   ");

        assertThatThrownBy(() -> service.addComment(ticketId, request, authorId))
                .isInstanceOf(InvalidCommentException.class);
    }

    @Test
    void should_set_comment_author() {
        var ticketId = UUID.randomUUID();
        var authorId = UUID.randomUUID();
        var request = new AddCommentRequest("This issue is still happening");

        when(repository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.addComment(ticketId, request, authorId);

        assertThat(result.getAuthorId()).isEqualTo(authorId);
    }

    @Test
    void should_set_comment_creation_date() {
        var ticketId = UUID.randomUUID();
        var authorId = UUID.randomUUID();
        var request = new AddCommentRequest("This issue is still happening");

        when(repository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.addComment(ticketId, request, authorId);

        assertThat(result.getCreatedAt()).isNotNull();
    }
}