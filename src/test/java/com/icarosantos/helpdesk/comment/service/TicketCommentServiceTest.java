package com.icarosantos.helpdesk.comment.service;

import com.icarosantos.helpdesk.comment.domain.TicketComment;
import com.icarosantos.helpdesk.comment.dto.AddCommentRequest;
import com.icarosantos.helpdesk.comment.repository.TicketCommentRepository;
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
    private TicketCommentRepository repository;

    @InjectMocks
    private TicketCommentService service;

    @Test
    void should_add_comment() {
        var ticketId = UUID.randomUUID();
        var authorId = UUID.randomUUID();
        var request = new AddCommentRequest("This issue is still happening", authorId);

        when(repository.save(any(TicketComment.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        var result = service.addComment(ticketId, request);

        assertThat(result.getTicketId()).isEqualTo(ticketId);
        assertThat(result.getContent()).isEqualTo("This issue is still happening");
    }

    @Test
    void should_reject_blank_comment() {
        var ticketId = UUID.randomUUID();
        var authorId = UUID.randomUUID();
        var request = new AddCommentRequest("   ", authorId);

        assertThatThrownBy(() -> service.addComment(ticketId, request))
                .isInstanceOf(InvalidCommentException.class);
    }
}