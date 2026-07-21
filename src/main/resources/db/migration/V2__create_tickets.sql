create table tickets (
    id uuid primary key,
    title varchar(100) not null,
    description varchar(1000) not null,
    status varchar(20) not null
                     check ( status in ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    priority varchar(20) not null
                     check ( priority in ('LOW', 'MEDIUM', 'HIGH')),
    created_by uuid not null references users(id),
    assigned_to uuid not null references users(id),
    created_at timestamp not null,
    updated_at timestamp not null
);

create index idx_tickets_status on tickets(status);
create index idx_tickets_priority on tickets(priority);
create index idx_tickets_created_by on tickets(created_by);
create index idx_tickets_assigned_to on tickets(assigned_to);