-- ------------------------------------------------------------
-- Dump of table payments
-- ------------------------------------------------------------

drop table if exists payments;
create table payments
(
    id            serial4   not null,
    date          timestamp not null,
    order_id      int4      not null,
    customer_id   int4      not null,
    type          int2      not null,
    amount        numeric   not null check ( amount > 0 ),
    is_deleted    boolean   not null,
    created_date  timestamp not null,
    modified_date timestamp not null,
    version       int8
);

alter table payments
    add constraint payment_id primary key (id);

comment on column payments.id is 'Payment id';
comment on column payments.date is 'Payment date';
comment on column payments.order_id is 'Order id';
comment on column payments.type is 'Payment type (0 - incoming, 1 - outgoing)';
comment on column payments.amount is 'Amount paid';
