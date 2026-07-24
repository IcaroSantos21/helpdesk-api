package com.icarosantos.helpdesk.comment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketComment {

    @Id
    private UUID id;

    private UUID ticketId;

    private UUID authorId;

    private String content;

    private LocalDateTime createdAt;
}
