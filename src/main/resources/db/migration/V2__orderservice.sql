-- ------------------------------------------------------------
-- Dump of table orders
-- ------------------------------------------------------------

drop table if exists orders;
create table orders
(
    id            uuid                  DEFAULT gen_random_uuid() not null,
    customer_id   int4         not null,
    address       varchar(255) not null default '',
    status        varchar(200)         not null,
    is_deleted    boolean      not null default false,
    created_date  timestamp    not null,
    modified_date timestamp    not null,
    version       int8
);

alter table orders
    add constraint order_id primary key (id);

comment on column orders.id is 'Order id';
comment on column orders.customer_id is 'Order customer';
comment on column orders.address is 'Order address';
comment on column orders.status is 'Order status';

-- ------------------------------------------------------------
-- Dump of table order items
-- ------------------------------------------------------------

drop table if exists order_items;
create table order_items
(
    id                     uuid DEFAULT gen_random_uuid() not null,
    order_id   uuid        not null,
    product_id int4        not null,
    quantity   int4        not null check ( quantity > 0 ),
    price      numeric     not null check ( price > 0 )
);

alter table order_items
    add constraint order_items_id primary key (id);
alter table order_items
    add constraint order_fk foreign key (order_id)
        references orders (id)
        on delete cascade;

comment on column order_items.id is 'Order line item id';
comment on column order_items.order_id is 'Order id';
comment on column order_items.product_id is 'Order items product id';
comment on column order_items.quantity is 'Order product quantity';
comment on column order_items.price is 'Order product price';
