package com.icarosantos.helpdesk.comment.dto;

import java.util.UUID;

public record AddCommentRequest(String content, UUID authorId) {
}
