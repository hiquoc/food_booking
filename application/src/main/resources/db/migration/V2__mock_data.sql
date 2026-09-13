-- =====================================================================
-- V2: Mock data
-- ---------------------------------------------------------------------
-- Seed accounts and demo data for local development:
--   ADMIN  0862470050
--   USER   0000000011
--   SHIPPER 0000000031
--   OWNER  0000000021
-- plus extra users, restaurant profiles, restaurants with menu sections
-- and items, orders (deliveries) and shipper assignments.
-- Inserts use ON CONFLICT so applying them is harmless if the rows
-- already exist.
-- All timestamps are in Vietnam time (UTC+07:00).
-- =====================================================================

-- ---------------------------------------------------------------------
-- users
-- ---------------------------------------------------------------------
INSERT INTO users (id, name, phone, role, banned, created_at, updated_at) VALUES
    ('b8382bc0-2072-4121-85e8-fbbd03bb616c', 'Nguyen Van Quan Tri', '0862470050', 'ADMIN',   FALSE, '2026-09-01T08:00:00+07:00', '2026-09-01T08:00:00+07:00'),
    ('8b18ec3d-9c54-4ad6-a2b8-aaa8e83798bf', 'Nguyen Van An',       '0000000011', 'USER',    FALSE, '2026-09-01T08:05:00+07:00', '2026-09-01T08:05:00+07:00'),
    ('3edc690e-476c-453b-9949-4c2060dc2a61', 'Tran Van Binh',       '0000000031', 'SHIPPER', FALSE, '2026-09-01T08:10:00+07:00', '2026-09-01T08:10:00+07:00'),
    ('03daf99b-94a8-4731-a8b9-21138fa12608', 'Le Van Chu',          '0000000021', 'OWNER',   FALSE, '2026-09-01T08:15:00+07:00', '2026-09-01T08:15:00+07:00'),
    ('199935c8-2805-4f72-ab9d-f12741d308c3', 'Pham Thi Bich',       '0000000012', 'USER',    FALSE, '2026-09-01T08:20:00+07:00', '2026-09-01T08:20:00+07:00'),
    ('4f99ce98-b361-4b24-99c1-2d17d7ba49bd', 'Hoang Van Dung',      '0000000032', 'SHIPPER', FALSE, '2026-09-01T08:25:00+07:00', '2026-09-01T08:25:00+07:00'),
    ('82fde488-b470-4fbc-b80a-a17905e2ac57', 'Do Van Hung',         '0000000022', 'OWNER',   FALSE, '2026-09-01T08:30:00+07:00', '2026-09-01T08:30:00+07:00')
ON CONFLICT (phone) DO NOTHING;

-- ---------------------------------------------------------------------
-- owner profiles (id = user id, see Owner @MapsId in the entity model)
-- ---------------------------------------------------------------------
INSERT INTO owners (id, user_id, created_at, updated_at) VALUES
    ('03daf99b-94a8-4731-a8b9-21138fa12608', '03daf99b-94a8-4731-a8b9-21138fa12608', '2026-09-01T08:15:00+07:00', '2026-09-01T08:15:00+07:00'),
    ('82fde488-b470-4fbc-b80a-a17905e2ac57', '82fde488-b470-4fbc-b80a-a17905e2ac57', '2026-09-01T08:30:00+07:00', '2026-09-01T08:30:00+07:00')
ON CONFLICT (user_id) DO NOTHING;

-- ---------------------------------------------------------------------
-- shipper profiles (id = user id, see Shipper @MapsId in the entity model)
-- ---------------------------------------------------------------------
INSERT INTO shippers (id, user_id, created_at, updated_at) VALUES
    ('3edc690e-476c-453b-9949-4c2060dc2a61', '3edc690e-476c-453b-9949-4c2060dc2a61', '2026-09-01T08:10:00+07:00', '2026-09-01T08:10:00+07:00'),
    ('4f99ce98-b361-4b24-99c1-2d17d7ba49bd', '4f99ce98-b361-4b24-99c1-2d17d7ba49bd', '2026-09-01T08:25:00+07:00', '2026-09-01T08:25:00+07:00')
ON CONFLICT (user_id) DO NOTHING;
-- ---------------------------------------------------------------------
-- restaurants
-- ---------------------------------------------------------------------
INSERT INTO restaurants (id, owner_id, name, phone, description, address, district, city,
                         latitude, longitude, open_hour, close_hour, status,
                         created_at, updated_at)
VALUES
    ('986afbcd-31d5-4da6-8bf9-f01f98a4e117', '03daf99b-94a8-4731-a8b9-21138fa12608',
     'Pho Viet 24', '0901111111', 'Quan pho truyen thong, nuoc dung dam da',
     '12 Nguyen Trai', 'Thanh Xuan', 'Ha Noi',
     20.995100, 105.815100, '06:00:00', '22:30:00', 'ACTIVE',
     '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    ('2647ca3b-d2c7-448d-8b39-e78004612b73', '03daf99b-94a8-4731-a8b9-21138fa12608',
     'Com Viet 365', '0902222222', 'Com van phong giao nhanh trong 30 phut',
     '45 Cau Giay', 'Cau Giay', 'Ha Noi',
     21.035600, 105.798600, '08:00:00', '21:30:00', 'ACTIVE',
     '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    ('c174b74b-f466-4b32-b544-8272764f032d', '82fde488-b470-4fbc-b80a-a17905e2ac57',
     'Tra Sua Hoa', '0903333333', 'Tra sua topping da dang, giao tan noi',
     '123 Hoang Hoa Tham', 'Ba Dinh', 'Ha Noi',
     21.032300, 105.823500, '09:00:00', '23:00:00', 'ACTIVE',
     '2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00')
ON CONFLICT (phone) DO NOTHING;

-- ---------------------------------------------------------------------
-- restaurant_sections
-- ---------------------------------------------------------------------
INSERT INTO restaurant_sections (id, restaurant_id, name, display_order, created_at, updated_at) VALUES
    (1, '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 'Mon Khai Vi', 0, '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (2, '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 'Mon Chinh',    1, '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (3, '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 'Do Uong',      2, '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (4, '2647ca3b-d2c7-448d-8b39-e78004612b73', 'Com Dia',      0, '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (5, '2647ca3b-d2c7-448d-8b39-e78004612b73', 'Mon An Kem',   1, '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (6, '2647ca3b-d2c7-448d-8b39-e78004612b73', 'Do Uong',      2, '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (7, 'c174b74b-f466-4b32-b544-8272764f032d', 'Tra Sua',      0, '2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00'),
    (8, 'c174b74b-f466-4b32-b544-8272764f032d', 'Topping',      1, '2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00')
ON CONFLICT (restaurant_id, name) DO NOTHING;

-- ---------------------------------------------------------------------
-- menu_items
-- ---------------------------------------------------------------------
INSERT INTO menu_items (id, restaurant_id, restaurant_section_id, name, image, price, discount_rate, display_order, status,version, created_at, updated_at) VALUES
    (1,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 1, 'Nem Ran',           'https://placehold.co/400x300?text=Nem+Ran',           45000.00, NULL, 0, 'AVAILABLE', 1,  '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (2,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 1, 'Goi Cuon',          'https://placehold.co/400x300?text=Goi+Cuon',          35000.00, 10.00, 1, 'AVAILABLE',1, '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (3,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 2, 'Pho Bo',            'https://placehold.co/400x300?text=Pho+Bo',            60000.00, 20.00, 0, 'AVAILABLE',1, '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (4,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 2, 'Pho Ga',            'https://placehold.co/400x300?text=Pho+Ga',            55000.00, NULL, 1, 'AVAILABLE',1,   '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (5,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 2, 'Bun Bo Hue',        'https://placehold.co/400x300?text=Bun+Bo+Hue',        65000.00, NULL, 2, 'AVAILABLE', 1,  '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (6,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 3, 'Tra Da',            'https://placehold.co/400x300?text=Tra+Da',            10000.00, NULL, 0, 'AVAILABLE', 1,  '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (7,  '986afbcd-31d5-4da6-8bf9-f01f98a4e117', 3, 'Nuoc Ngot',         'https://placehold.co/400x300?text=Nuoc+Ngot',         15000.00, NULL, 1, 'AVAILABLE', 1,  '2026-09-01T09:00:00+07:00', '2026-09-01T09:00:00+07:00'),
    (8,  '2647ca3b-d2c7-448d-8b39-e78004612b73', 4, 'Com Suon Nuong',    'https://placehold.co/400x300?text=Com+Suon+Nuong',    55000.00, 30.00, 0, 'AVAILABLE', 1,'2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (9,  '2647ca3b-d2c7-448d-8b39-e78004612b73', 4, 'Com Ga Xoi Mo',     'https://placehold.co/400x300?text=Com+Ga+Xoi+Mo',     50000.00, NULL, 1, 'AVAILABLE', 1,  '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (10, '2647ca3b-d2c7-448d-8b39-e78004612b73', 5, 'Cha Gio',           'https://placehold.co/400x300?text=Cha+Gio',           40000.00, NULL, 0, 'AVAILABLE', 1,  '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (11, '2647ca3b-d2c7-448d-8b39-e78004612b73', 6, 'Nuoc Cam Ep',       'https://placehold.co/400x300?text=Nuoc+Cam+Ep',       25000.00, NULL, 0, 'AVAILABLE', 1,  '2026-09-01T09:10:00+07:00', '2026-09-01T09:10:00+07:00'),
    (12, 'c174b74b-f466-4b32-b544-8272764f032d', 7, 'Tra Sua Tran Chau', 'https://placehold.co/400x300?text=Tra+Sua+Tran+Chau',  35000.00, 33.00, 0, 'AVAILABLE', 1,'2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00'),
    (13, 'c174b74b-f466-4b32-b544-8272764f032d', 7, 'Tra Sua Matcha',    'https://placehold.co/400x300?text=Tra+Sua+Matcha',    40000.00, NULL, 1, 'AVAILABLE',  1, '2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00'),
    (14, 'c174b74b-f466-4b32-b544-8272764f032d', 8, 'Tran Chau Duong Den', 'https://placehold.co/400x300?text=Tran+Chau+Duong+Den', 10000.00, NULL, 0, 'AVAILABLE', 1,'2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00'),
    (15, 'c174b74b-f466-4b32-b544-8272764f032d', 8, 'Pudding Ca Phe',    'https://placehold.co/400x300?text=Pudding+Ca+Phe',    12000.00, NULL, 1, 'AVAILABLE', 1,  '2026-09-01T09:20:00+07:00', '2026-09-01T09:20:00+07:00')
ON CONFLICT (id) DO NOTHING;
-- ---------------------------------------------------------------------
-- deliveries
-- ---------------------------------------------------------------------
INSERT INTO deliveries (id, code, user_id, status, subtotal, discounted_amount, shipping_fee,
                        total_amount, distance, shipper_revenue, expected_delivery_at, delivered_at,
                        created_at, updated_at)
VALUES
    ('92ceae30-c33e-48da-9ccc-35abf580e0d9', 'DEMO-20260901-0001', '8b18ec3d-9c54-4ad6-a2b8-aaa8e83798bf',
     'DELIVERED', 165000.00, 15000.00, 20000.00, 170000.00, 3.50, 20000.00,
     '2026-09-01T12:30:00+07:00', '2026-09-01T13:08:00+07:00',
     '2026-09-01T11:40:00+07:00', '2026-09-01T13:08:00+07:00'),
    ('59e4654d-dc8a-41a8-9778-d26817e28325', 'DEMO-20260902-0002', '8b18ec3d-9c54-4ad6-a2b8-aaa8e83798bf',
     'CONFIRMED', 115000.00, 0.00, 15000.00, 130000.00, 2.30, 15000.00,
     '2026-09-02T13:00:00+07:00', NULL,
     '2026-09-02T11:20:00+07:00', '2026-09-02T11:35:00+07:00'),
    ('43979a9f-cbfe-4b07-8816-bc4bb8dd7a1e', 'DEMO-20260903-0003', '199935c8-2805-4f72-ab9d-f12741d308c3',
     'PREPARING', 72000.00, 5000.00, 15000.00, 82000.00, 1.80, 15000.00,
     '2026-09-03T12:15:00+07:00', NULL,
     '2026-09-03T10:50:00+07:00', '2026-09-03T11:10:00+07:00'),
    ('f469b863-a9a6-4b8b-af39-28aca282e69a', 'DEMO-20260905-0004', '8b18ec3d-9c54-4ad6-a2b8-aaa8e83798bf',
     'PICKED_UP', 140000.00, 15000.00, 20000.00, 145000.00, 4.10, 20000.00,
     '2026-09-05T12:45:00+07:00', NULL,
     '2026-09-05T11:30:00+07:00', '2026-09-05T12:10:00+07:00'),
    ('e615afe9-9d95-45a4-af7b-0552c0b9df3f', 'DEMO-20260908-0005', '199935c8-2805-4f72-ab9d-f12741d308c3',
     'CREATED', 90000.00, 0.00, 20000.00, 110000.00, 2.00, NULL,
     '2026-09-08T18:00:00+07:00', NULL,
     '2026-09-08T10:15:00+07:00', '2026-09-08T10:15:00+07:00')
ON CONFLICT (code) DO NOTHING;

-- ---------------------------------------------------------------------
-- delivery_items (snapshots of the menu items at purchase time)
-- ---------------------------------------------------------------------
INSERT INTO delivery_items (id, delivery_id, menu_item_id, item_name, unit_price,line_total, discount_rate, quantity, created_at, updated_at) VALUES
    (1,  '92ceae30-c33e-48da-9ccc-35abf580e0d9', 3,  'Pho Bo',    54000.00,108000.00, 10.00, 2, '2026-09-01T11:40:00+07:00', '2026-09-01T11:40:00+07:00'),
    (2,  '92ceae30-c33e-48da-9ccc-35abf580e0d9', 1,  'Nem Ran',   45000.00,45000.00, NULL,      1, '2026-09-01T11:40:00+07:00', '2026-09-01T11:40:00+07:00'),
    (3,  '92ceae30-c33e-48da-9ccc-35abf580e0d9', 6,  'Tra Da',    10000.00,10000.00, NULL,      1, '2026-09-01T11:40:00+07:00', '2026-09-01T11:40:00+07:00'),
    (4,  '59e4654d-dc8a-41a8-9778-d26817e28325', 8,  'Com Suon Nuong', 44000.00,44000.00, 20.00, 1, '2026-09-02T11:20:00+07:00', '2026-09-02T11:20:00+07:00'),
    (5,  '59e4654d-dc8a-41a8-9778-d26817e28325', 11, 'Nuoc Cam Ep',    25000.00,25000.00, NULL, 1, '2026-09-02T11:20:00+07:00', '2026-09-02T11:20:00+07:00'),
    (6,  '59e4654d-dc8a-41a8-9778-d26817e28325', 10, 'Cha Gio',        40000.00,40000.00, NULL, 1, '2026-09-02T11:20:00+07:00', '2026-09-02T11:20:00+07:00'),
    (7,  '43979a9f-cbfe-4b07-8816-bc4bb8dd7a1e', 12, 'Tra Sua Tran Chau', 31500.00,63000.00, 10.00, 2, '2026-09-03T10:50:00+07:00', '2026-09-03T10:50:00+07:00'),
    (8,  '43979a9f-cbfe-4b07-8816-bc4bb8dd7a1e', 15, 'Pudding Ca Phe',    12000.00,12000.00, NULL, 1, '2026-09-03T10:50:00+07:00', '2026-09-03T10:50:00+07:00'),
    (9,  'f469b863-a9a6-4b8b-af39-28aca282e69a', 3,  'Pho Bo',    20000.00, 20000.00, 33.00, 1, '2026-09-05T11:30:00+07:00', '2026-09-05T11:30:00+07:00'),
    (10, 'f469b863-a9a6-4b8b-af39-28aca282e69a', 4,  'Pho Ga',    55000.00,55000.00, NULL,      1, '2026-09-05T11:30:00+07:00', '2026-09-05T11:30:00+07:00'),
    (11, 'f469b863-a9a6-4b8b-af39-28aca282e69a', 7,  'Nuoc Ngot', 15000.00,30000.00, NULL,      2, '2026-09-05T11:30:00+07:00', '2026-09-05T11:30:00+07:00'),
    (12, 'e615afe9-9d95-45a4-af7b-0552c0b9df3f', 1,  'Nem Ran',   45000.00,70000.00, NULL,      2, '2026-09-08T10:15:00+07:00', '2026-09-08T10:15:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------
-- delivery_assignments
-- ---------------------------------------------------------------------
INSERT INTO delivery_assignments (id, delivery_id, shipper_id, sequence_number, status, expires_at, created_at, updated_at) VALUES
    (1, '92ceae30-c33e-48da-9ccc-35abf580e0d9', '3edc690e-476c-453b-9949-4c2060dc2a61', 1, 'DELIVERED',        '2026-09-01T12:00:00+07:00', '2026-09-01T11:50:00+07:00', '2026-09-01T13:08:00+07:00'),
    (2, '59e4654d-dc8a-41a8-9778-d26817e28325', '4f99ce98-b361-4b24-99c1-2d17d7ba49bd', 1, 'OFFERED',          '2026-09-02T13:30:00+07:00', '2026-09-02T11:40:00+07:00', '2026-09-02T11:40:00+07:00'),
    (3, 'f469b863-a9a6-4b8b-af39-28aca282e69a', '3edc690e-476c-453b-9949-4c2060dc2a61', 1, 'GOING_TO_CUSTOMER', '2026-09-05T12:00:00+07:00', '2026-09-05T11:55:00+07:00', '2026-09-05T12:15:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------
-- Advance identity sequences past the explicitly inserted ids so future
-- JPA inserts do not collide.
-- ---------------------------------------------------------------------
SELECT setval(pg_get_serial_sequence('restaurant_sections', 'id'), (SELECT COALESCE(MAX(id), 1) FROM restaurant_sections));
SELECT setval(pg_get_serial_sequence('menu_items', 'id'), (SELECT COALESCE(MAX(id), 1) FROM menu_items));
SELECT setval(pg_get_serial_sequence('delivery_items', 'id'), (SELECT COALESCE(MAX(id), 1) FROM delivery_items));
SELECT setval(pg_get_serial_sequence('delivery_assignments', 'id'), (SELECT COALESCE(MAX(id), 1) FROM delivery_assignments));