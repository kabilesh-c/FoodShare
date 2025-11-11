-- Sample data for FoodShare application - PostgreSQL Compatible
-- Login credentials (ALL USE PASSWORD: password123):
-- admin@foodshare.com / password123
-- donor@example.com / password123
-- receiver@example.com / password123

-- BCrypt hash for 'password123': $2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2
-- This password works for all demo accounts

INSERT INTO users (id, name, email, password, phone, city, role, created_at) VALUES
(gen_random_uuid(), 'Admin User', 'admin@foodshare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '1234567890', 'Chennai', 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Admin Gmail', 'admin@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '1234567891', 'Chennai', 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Donor Demo', 'donor@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '9876543210', 'Bengaluru', 'ROLE_DONOR', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Receiver Demo', 'receiver@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '9876554321', 'Delhi', 'ROLE_RECEIVER', CURRENT_TIMESTAMP);

-- Sample report data for analytics
INSERT INTO reports (id, report_date, total_donations, total_food_saved_kg, total_receivers_served) VALUES
(gen_random_uuid(), CURRENT_DATE - INTERVAL '30 days', 15, 85.5, 42),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '29 days', 18, 102.3, 48),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '28 days', 12, 67.8, 35),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '25 days', 22, 125.7, 58),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '22 days', 20, 115.2, 52),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '20 days', 16, 89.4, 41),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '18 days', 25, 142.6, 65),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '15 days', 28, 156.8, 72),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '12 days', 19, 108.3, 47),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '10 days', 23, 131.5, 59),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '8 days', 21, 118.9, 54),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '5 days', 30, 172.4, 78),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '3 days', 26, 148.7, 68),
(gen_random_uuid(), CURRENT_DATE - INTERVAL '1 days', 24, 135.6, 61),
(gen_random_uuid(), CURRENT_DATE, 27, 152.3, 70);

-- Sample donation data for charts and analytics  
INSERT INTO donation (id, donor_id, food_name, food_type, quantity, description, expiry_date, city, status, created_at, image_url) VALUES
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Fresh Vegetables', 'VEGETABLES', '5 kg', 'Fresh organic vegetables from our farm', CURRENT_DATE + INTERVAL '2 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '15 days', '/images/vegetables.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Cooked Rice', 'COOKED_FOOD', '10 servings', 'Freshly cooked rice for evening distribution', CURRENT_DATE + INTERVAL '1 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '12 days', '/images/rice.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Bread Loaves', 'BAKERY', '20 loaves', 'Fresh bread from bakery surplus', CURRENT_DATE + INTERVAL '3 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '10 days', '/images/bread.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Milk Products', 'DAIRY', '15 packets', 'UHT milk packets, good condition', CURRENT_DATE + INTERVAL '7 days', 'Bengaluru', 'claimed', CURRENT_TIMESTAMP - INTERVAL '8 days', '/images/milk.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Mixed Fruits', 'FRUITS', '8 kg', 'Seasonal fruits - apples, bananas, oranges', CURRENT_DATE + INTERVAL '4 days', 'Bengaluru', 'claimed', CURRENT_TIMESTAMP - INTERVAL '6 days', '/images/fruits.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Pasta', 'DRY_GOODS', '25 packets', 'Unopened pasta packets', CURRENT_DATE + INTERVAL '90 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '5 days', '/images/pasta.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Prepared Meals', 'COOKED_FOOD', '30 meals', 'Restaurant surplus meals in good condition', CURRENT_DATE + INTERVAL '1 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '3 days', '/images/meals.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Canned Goods', 'CANNED', '40 cans', 'Various canned vegetables and soups', CURRENT_DATE + INTERVAL '365 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '2 days', '/images/canned.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Biscuits & Snacks', 'PACKAGED', '50 packets', 'Sealed snack packets, good for distribution', CURRENT_DATE + INTERVAL '30 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP - INTERVAL '1 days', '/images/snacks.jpg'),
(gen_random_uuid(), (SELECT id FROM users WHERE email = 'donor@example.com'), 'Fresh Salad', 'VEGETABLES', '12 portions', 'Pre-made fresh salad portions', CURRENT_DATE + INTERVAL '1 days', 'Bengaluru', 'available', CURRENT_TIMESTAMP, '/images/salad.jpg');
