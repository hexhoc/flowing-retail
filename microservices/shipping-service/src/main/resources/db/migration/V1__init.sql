
-- ------------------------------------------------------------
-- Dump of table waybills
-- ------------------------------------------------------------

drop table if exists waybills;
create table waybills
(
    id                 uuid DEFAULT gen_random_uuid() not null,
    waybill_date       timestamp                      not null,
    order_id           varchar(36)                    not null,
    recipient_name     varchar(255)                   not null,
    recipient_address  varchar(500)                   not null,
    logistics_provider varchar(50)                    not null
);

alter table waybills
    add constraint waybill_pk primary key (id);

comment on column waybills.id is 'Waybill id';
comment on column waybills.waybill_date is 'Waybill date';
comment on column waybills.recipient_name is 'Waybill name of recipient';
comment on column waybills.recipient_address is 'Complete address the shipment is sent to';
comment on column waybills.logistics_provider is 'Delivering the shipment (e.g. DHL, UPS, ...)';
