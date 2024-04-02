-- ------------------------------------------------------------
-- Dump of table categories
-- ------------------------------------------------------------

drop table if exists "categories";
create table "categories"
(
    id            serial4,
    level         int2         not null,
    parent_id     int4,
    name          varchar(100) not null,
    rank          int          not null,
    is_deleted    boolean      not null,
    created_date  timestamp    not null,
    modified_date timestamp    not null,
    version       int8
);
alter table "categories"
    add constraint "category_pk" primary key ("id");

comment on column categories.id is 'Category ID';
comment on column categories.level is 'Category level (1-first-level classification 2-secondary classification 3-third-level classification)';
comment on column categories.parent_id is 'parent category id';
comment on column categories.name is 'Category name';
comment on column categories.rank is 'Sort value (the bigger the field, the higher the front)';
comment on column categories.is_deleted is 'Delete identification field (false-not deleted true-deleted)';
comment on column categories.created_date is 'Creation time';
comment on column categories.modified_date is 'Modification time';
comment on column categories.version is 'current version of object';

-- ------------------------------------------------------------
-- Dump of table products
-- ------------------------------------------------------------

drop table if exists "products";
create table "products"
(
    id             serial4,
    name           varchar(300) not null default '',
    intro          varchar(500) not null default '',
    description    varchar(500) not null default '',
    category_id    int4         not null,
    original_price numeric      not null default 0 check ( original_price > 0 ),
    selling_price  numeric      not null default 0 check ( selling_price > 0 ),
    is_sale        boolean      not null default false,
    is_deleted     boolean      not null default false,
    created_date   timestamp    not null,
    modified_date  timestamp    not null,
    version        int8
);

alter table products
    add constraint "product_id" primary key (id);
alter table products
    add constraint "product_fk" foreign key (category_id)
        references categories (id)
        on delete set null;

comment on column products.name is 'Product name';
comment on column products.intro is 'Product introduction';
comment on column products.description is 'Product details';
comment on column products.category_id is 'Associated category id';
comment on column products.original_price is 'Product purchase price';
comment on column products.selling_price is 'Product selling price';
comment on column products.is_sale is 'True if the product is on the shelf, and false if it is removed from the shelf';
comment on column products.is_deleted is 'Delete identification field (false-not deleted true-deleted)';
comment on column products.created_date is 'Creation time';
comment on column products.modified_date is 'Modification time';
comment on column products.version is 'current version of object';

-- ------------------------------------------------------------
-- Dump of table tags
-- ------------------------------------------------------------

drop table if exists tags;
create table tags
(
    id          serial4,
    name        varchar(50) not null default '',
    description varchar(50) not null default ''
);

alter table tags
    add constraint tag_id primary key (id);

comment on column tags.id is 'Tag id';
comment on column tags.name is 'Tag name';
comment on column tags.description is 'Tag description';

-- ------------------------------------------------------------
-- Dump of table product_tag
-- ------------------------------------------------------------

drop table if exists product_tag;
create table product_tag
(
    product_id int4 not null,
    tag_id     int4 not null
);

alter table product_tag
    add constraint product_fk foreign key (product_id)
        references products (id)
        on delete cascade;
alter table product_tag
    add constraint tag_fk foreign key (tag_id)
        references tags (id)
        on delete cascade;

comment on column product_tag.product_id is 'Product id';
comment on column product_tag.tag_id is 'Tag id';


-- ------------------------------------------------------------
-- Dump of table reviews
-- ------------------------------------------------------------

drop table if exists reviews;
create table reviews
(
    id            serial4   not null,
    product_id   int4      not null,
    customer_id   int4      not null,
    rating        int2      not null,
    content       text      not null,
    is_deleted    boolean   not null,
    created_date  timestamp not null,
    modified_date timestamp not null,
    version       int8
);

alter table reviews
    add constraint review_id primary key (id);

alter table reviews
    add constraint product_fk foreign key (product_id)
        references products (id)
        on delete cascade;
-- alter table reviews
--     add constraint customer_fk foreign key (customer_id)
--         references customers (id)
--         on delete no action;


-- ------------------------------------------------------------
-- Dump of table images
-- ------------------------------------------------------------

drop table if exists product_images;
create table product_images
(
    id          serial4 not null,
    product_id  int4 not null,
    name        varchar(255) not null,
    image_bytes bytea
);

alter table product_images
    add constraint image_pk primary key (id);

alter table product_images
    add constraint product_fk foreign key (product_id)
        references products (id)
        on delete cascade;

comment on column product_images.id is 'image id';
comment on column product_images.product_id is 'product id';
comment on column product_images.name is 'image name (or full path)';
comment on column product_images.image_bytes is 'image binary';