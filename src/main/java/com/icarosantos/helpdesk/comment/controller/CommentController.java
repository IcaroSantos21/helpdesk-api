package com.icarosantos.helpdesk.comment.controller;

import com.icarosantos.helpdesk.comment.dto.AddCommentRequest;
import com.icarosantos.helpdesk.comment.dto.CommentResponse;
import com.icarosantos.helpdesk.comment.service.CommentService;
import com.icarosantos.helpdesk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody AddCommentRequest request, @PathVariable UUID ticketId, Authentication authentication) {
        var authorId = userRepository.findByEmail(authentication.getName()).orElseThrow().getId();
        var comment = commentService.addComment(ticketId, request, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(comment));
    }
}
