-- Mock data for categories table
INSERT INTO categories (level, parent_id, name, rank, is_deleted, created_date, modified_date, version)
VALUES (1, null, 'Electronics', 1, false, '2022-10-01 10:00:00', '2022-10-01 10:00:00', 1),
       (2, 1, 'Computers', 1, false, '2022-10-02 10:00:00', '2022-10-02 10:00:00', 1),
       (2, 1, 'Smartphones', 2, false, '2022-10-03 10:00:00', '2022-10-03 10:00:00', 1),
       (3, 2, 'Laptops', 1, false, '2022-10-04 10:00:00', '2022-10-04 10:00:00', 1),
       (3, 2, 'Desktops', 2, false, '2022-10-05 10:00:00', '2022-10-05 10:00:00', 1),
       (3, 3, 'Apple', 1, false, '2022-10-06 10:00:00', '2022-10-06 10:00:00', 1),
       (3, 3, 'Samsung', 2, false, '2022-10-07 10:00:00', '2022-10-07 10:00:00', 1);

INSERT INTO products (name, intro, description, category_id, original_price, selling_price, is_sale, is_deleted, created_date, modified_date, version)
VALUES ('MacBook Pro', 'Powerful laptop from Apple', 'The MacBook Pro features a stunning Retina display, advanced processors, superfast graphics, and more.', 4, 1599.00, 1799.00, false, false, '2022-10-01 10:00:00', '2022-10-01 10:00:00', 1),
       ('Galaxy S21', 'Samsung flagship smartphone', 'The Galaxy S21 features a 6.2-inch AMOLED display, 5G connectivity, and a powerful Exynos 2100 processor.', 7, 799.00, 899.00, true, false, '2022-10-02 10:00:00', '2022-10-02 10:00:00', 1),
       ('iMac', 'All-in-one desktop from Apple', 'The iMac features a beautiful 24-inch Retina display, powerful Apple M1 chip, and a sleek, modern design.', 5, 1299.00, 1499.00, false, false, '2022-10-03 10:00:00', '2022-10-03 10:00:00', 1),
       ('Galaxy Book Pro', 'Samsung ultrabook', 'The Galaxy Book Pro is an ultra-thin and light laptop with a stunning AMOLED display, 11th Gen Intel Core processor, and 5G connectivity.', 4, 1199.00, 1399.00, true, false, '2022-10-04 10:00:00', '2022-10-04 10:00:00', 1),
       ('Mac mini', 'Powerful small desktop from Apple', 'The Mac mini is a compact desktop with the latest Apple M1 chip, up to 16GB of RAM, and plenty of ports for connectivity.', 5, 699.00, 799.00, false, false, '2022-10-05 10:00:00', '2022-10-05 10:00:00', 1),
       ('Galaxy Tab S7', 'Samsung Android tablet', 'The Galaxy Tab S7 features a 12.4-inch Super AMOLED display, 5G connectivity, and the powerful Snapdragon 865+ processor.', 7, 649.00, 749.00, true, false, '2022-10-06 10:00:00', '2022-10-06 10:00:00', 1),
       ('MacBook Air', 'Thin and light laptop from Apple', 'The MacBook Air is a fan-favorite, with a 13.3-inch Retina display, Apple M1 chip, and up to 18 hours of battery life.', 4, 999.00, 1199.00, false, false, '2022-10-07 10:00:00', '2022-10-07 10:00:00', 1);

INSERT INTO reviews (product_id, customer_id, rating, content, is_deleted, created_date, modified_date, version)
VALUES (1, 1, 5, 'Great computer, runs smoothly and is very fast. Highly recommend!', false, '2022-10-10 10:00:00', '2022-10-10 10:00:00', 1),
       (1, 2, 3, 'This computer is okay, but it sometimes freezes and is slower than I expected. Overall decent value for the price.', false, '2022-10-11 10:00:00', '2022-10-11 10:00:00', 1),
       (2, 1, 4, 'Really happy with this phone, the camera quality is excellent and the battery life is great.', false, '2022-10-12 10:00:00', '2022-10-12 10:00:00', 1),
       (2, 2, 2, 'I have had some issues with this phone, it sometimes freezes and is slower than I expected. Camera is decent but not great.', false, '2022-10-13 10:00:00', '2022-10-13 10:00:00', 1),
       (3, 1, 4, 'Great laptop, very fast and light. Battery life is good and the display is high quality.', false, '2022-10-14 10:00:00', '2022-10-14 10:00:00', 1),
       (4, 2, 3, 'This desktop is okay, but it is slower than I expected and takes a long time to boot up. Decent value for the price.', false, '2022-10-15 10:00:00', '2022-10-15 10:00:00', 1),
       (5, 1, 5, 'Amazing computer, runs smoothly and is very fast. Highly recommend!', false, '2022-10-16 10:00:00', '2022-10-16 10:00:00', 1),
       (6, 2, 4, 'Great phone, very sleek and fast. Camera is high quality and battery life is good.', false, '2022-10-17 10:00:00', '2022-10-17 10:00:00', 1),
       (7, 1, 3, 'This phone is okay, but I expected more from an expensive device. Camera is decent but not great and battery life could be better.', false, '2022-10-18 10:00:00', '2022-10-18 10:00:00', 1);

-- mock data for tags table
INSERT INTO tags (name, description)
VALUES ('Electronics', 'Products related to electronics'),
       ('Laptops', 'Portable computers'),
       ('Gaming', 'Products related to gaming'),
       ('Phones', 'Mobile phones'),
       ('Accessories', 'Accessories for electronics');

-- mock data for product_tag table
INSERT INTO product_tag (product_id, tag_id)
VALUES (1, 1), -- Product 1 is tagged as Electronics
       (1, 2), -- Product 1 is also tagged as Laptops
       (2, 1), -- Product 2 is also tagged as Electronics
       (2, 3), -- Product 2 is tagged as Gaming
       (3, 1), -- Product 3 is also tagged as Electronics
       (3, 4), -- Product 3 is tagged as Phones
       (4, 5); -- Product 4 is tagged as Accessories

