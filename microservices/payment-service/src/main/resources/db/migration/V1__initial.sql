drop table if exists payment_invoices;
create table payment_invoices
(
    id           uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    date         timestamp,
    customer_id  uuid,
    order_status varchar(50),
    total_due    numeric
);