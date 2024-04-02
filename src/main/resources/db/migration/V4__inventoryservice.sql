-- ------------------------------------------------------------
-- Dump of table warehouse
-- ------------------------------------------------------------

drop table if exists warehouses;
create table warehouses
(
    id      int4         not null,
    name    varchar(255) not null,
    address varchar(500) not null default 0
);

alter table warehouses
    add constraint warehouse_id primary key (id);

comment on column warehouses.id is 'Warehouse id';
comment on column warehouses.name is 'Warehouse name';
comment on column warehouses.address is 'Warehouse full address';

-- ------------------------------------------------------------
-- Dump of table product stocks
-- ------------------------------------------------------------

drop table if exists product_stocks;
create table product_stocks
(
    product_id   int4 not null,
    warehouse_id int4 not null,
    stock        int4 not null default 0
);

alter table product_stocks
    add constraint pk_product_stocks primary key (product_id, warehouse_id);


alter table product_stocks
    add constraint warehouse_fk foreign key (warehouse_id)
        references warehouses (id)
        on delete cascade;

comment on column product_stocks.product_id is 'Product id';
comment on column product_stocks.warehouse_id is 'Warehouse where the remains are located';
comment on column product_stocks.stock is 'Total stock';
