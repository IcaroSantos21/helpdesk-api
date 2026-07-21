create table users (
    id uuid primary key,
    name varchar(120) not null,
    email varchar(150) not null unique,
    password varchar(255) not null,
    role_user varchar(20) not null
                   check ( role_user in ('CLIENT', 'AGENT', 'ADMIN'))
);

create unique index uk_users_email on users(email);