drop table if exists customers;
create table customers
(
    id           uuid                  DEFAULT gen_random_uuid() PRIMARY KEY,
    name         varchar(255) not null,
    address      varchar(255) not null default '',
    api_key      varchar(36),
    created_date timestamp,
    updated_date timestamp,
    version      int
);


insert into customers (id, name, address, api_key, created_date, updated_date, version)
values
    ('e8f5cc5a-af5a-4a94-a08c-fe22190aa036', 'John Tompson', 'Germany, sesame street, 21', 'd8f5cc5a-af5a-4a94-a08c-fe22190aa036', '2022-10-02 14:52:45.024768', '2022-10-02 14:52:45.025767', 0),
    ('e8f5cc5a-af5a-4a94-a08c-fe22190aa037', 'Tomas Harnlick', 'Germany, sesame street, 21', 'd8f5cc5a-af5a-4a94-a08c-fe22190aa037', '2022-10-02 14:52:45.024768', '2022-10-02 14:52:45.025767', 0);