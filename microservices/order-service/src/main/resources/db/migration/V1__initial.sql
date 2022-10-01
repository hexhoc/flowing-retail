drop table if exists customers;
create table customers
(
    id           uuid                  DEFAULT gen_random_uuid() PRIMARY KEY,
    name         varchar(255) not null,
    address      varchar(255) not null default '',
    created_date timestamp,
    updated_date timestamp,
    version      int
);

drop table if exists orders;
create table orders
(
    id           uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id  uuid,
    created_date timestamp,
    updated_date timestamp,
    version      int,
    constraint fk_orders_customer_id foreign key (customer_id) references customers (id) on delete set null
);

drop table if exists order_items;
create table order_items
(
    id         uuid                  DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id   uuid,
    article_id varchar(255) not null default '',
    amount     int          not null default 0,
    constraint fk_order_items_order_id foreign key (order_id) references orders (id) on delete set null
);
