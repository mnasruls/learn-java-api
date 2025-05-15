begin;
create extension if not exists "uuid-ossp";
create table addresses (
    id uuid primary key default uuid_generate_v4(),
    contact_id uuid not null,
    street varchar(255) null,
    city varchar(255) null,
    province varchar(255) null,
    country varchar(255) not null,
    postal_code varchar(255) null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    foreign key (contact_id) references contacts(id)
);
commit;