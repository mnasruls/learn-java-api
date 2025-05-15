begin;
create extension if not exists "uuid-ossp";
create table contacts (
    id uuid primary key default uuid_generate_v4(),
    user_id uuid not null,
    first_name varchar(255) not null,
    last_name varchar(255) null,
    email varchar(255) null,
    phone varchar(255) null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    foreign key (user_id) references users(id) on delete cascade
);
commit;