begin;
create extension if not exists "uuid-ossp";
create table users (
    id uuid primary key default uuid_generate_v4(),
    name varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    token varchar(255) null,
    token_expired_at bigint null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(), 
    unique (username, token)
); 
commit;