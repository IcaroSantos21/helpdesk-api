package com.icarosantos.helpdesk.comment.repository;

import com.icarosantos.helpdesk.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
