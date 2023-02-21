insert into warehouses(id, name, address)
values (1, 'main', 'Germany, Rheinland-Pfalz, Sprendlingen city, street Guentzelstrasse 23');

insert into product_stocks(product_id, warehouse_id, stock)
values (1, 1, 10),
       (2, 1, 10),
       (3, 1, 10),
       (4, 1, 10),
       (5, 1, 10),
       (6, 1, 10),
       (7, 1, 10);