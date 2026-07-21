create table tickets_comments (
    id uuid primary key,
    ticket_id uuid not null references tickets(id),
    authors_id uuid not null references users(id),
    message varchar(100) not null,
    created_at timestamp not null
);

create index idx_comments_ticket on tickets_comments(ticket_id)