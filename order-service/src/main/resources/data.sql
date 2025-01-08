-- Insert data into Orders table
INSERT INTO orders (total_price, order_status) VALUES
                                       (100.50, 'PENDING'),
                                       (250.00, 'COMPLETED'),
                                       (75.20, 'CANCELLED'),
                                       (300.75, 'PENDING'),
                                       (500.00, 'COMPLETED'),
                                       (150.25, 'CANCELLED'),
                                       (200.40, 'PENDING'),
                                       (400.60, 'COMPLETED'),
                                       (125.00, 'PENDING');

-- Insert data into OrderItem table
INSERT INTO order_item (order_id, product_id, quantity) VALUES
                                                            (1, 101, 2),
                                                            (1, 102, 1),
                                                            (2, 103, 1),
                                                            (2, 104, 3),
                                                            (3, 105, 1),
                                                            (4, 106, 5),
                                                            (4, 107, 2),
                                                            (5, 108, 4),
                                                            (5, 109, 1),
                                                            (6, 110, 2),
                                                            (6, 111, 3),
                                                            (7, 112, 1),
                                                            (7, 113, 6),
                                                            (8, 114, 2),
                                                            (8, 115, 1),
                                                            (8, 116, 4),
                                                            (9, 117, 3),
                                                            (9, 118, 2);
