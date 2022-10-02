drop table if exists orders;
create table orders
(
    id           uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id  uuid,
    order_status varchar(50),
    created_date timestamp,
    updated_date timestamp,
    version      int
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
