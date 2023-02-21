-- ------------------------------------------------------------
-- Dump of table customer
-- ------------------------------------------------------------

drop table if exists customers;
create table customers
(
    id            serial4,
    first_name    varchar(50) not null default '',
    last_name     varchar(50) not null default '',
    email         varchar(50),
    phone         varchar(50),
    is_deleted    boolean     not null,
    created_date  timestamp   not null,
    modified_date timestamp   not null,
    version       int8
);

alter table customers
    add constraint customer_id primary key (id);

comment on column customers.id is 'Customer id';
comment on column customers.first_name is 'Customer first name';
comment on column customers.last_name is 'Customer last name';
comment on column customers.email is 'Customer email';
comment on column customers.phone is 'Customer phone';
