-- =====================================================
-- CLEAN DATA.SQL - Demo Data Insertion
-- =====================================================
-- Run this AFTER CLEAN_INIT.sql
-- This will populate your database with demo data
-- =====================================================

-- =====================================================
-- STEP 1: INSERT DEMO USERS
-- =====================================================
-- Password for ALL users: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2

-- Admin User
INSERT INTO users (name, email, password, phone, city, role, created_at) 
VALUES (
    'Admin User', 
    'admin@foodshare.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', 
    '1234567890', 
    'Chennai', 
    'ROLE_ADMIN', 
    NOW()
);

-- Donor User 1
INSERT INTO users (name, email, password, phone, city, role, created_at) 
VALUES (
    'Donor Demo', 
    'donor@example.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', 
    '9876543210', 
    'Bengaluru', 
    'ROLE_DONOR', 
    NOW()
);

-- Receiver User 1
INSERT INTO users (name, email, password, phone, city, role, created_at) 
VALUES (
    'Receiver Demo', 
    'receiver@example.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', 
    '9876554321', 
    'Delhi', 
    'ROLE_RECEIVER', 
    NOW()
);

-- Donor User 2 (Hotel)
INSERT INTO users (name, email, password, phone, city, role, created_at) 
VALUES (
    'Hotel Taj Manager', 
    'hotel@example.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', 
    '9123456789', 
    'Mumbai', 
    'ROLE_DONOR', 
    NOW()
);

-- Receiver User 2 (NGO)
INSERT INTO users (name, email, password, phone, city, role, created_at) 
VALUES (
    'NGO Help Foundation', 
    'ngo@example.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', 
    '9234567890', 
    'Delhi', 
    'ROLE_RECEIVER', 
    NOW()
);

-- =====================================================
-- STEP 2: INSERT SAMPLE DONATIONS
-- =====================================================

-- Donation 1: Fresh Vegetables (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Fresh Vegetables',
    'VEG',
    '5 kg',
    'Fresh organic vegetables from our farm including tomatoes, carrots, and leafy greens',
    CURRENT_DATE + INTERVAL '2 days',
    'Bengaluru',
    'available',
    '/images/vegetables.jpg',
    NOW() - INTERVAL '15 days'
FROM users WHERE email = 'donor@example.com';

-- Donation 2: Cooked Rice (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Cooked Rice',
    'VEG',
    '10 servings',
    'Freshly cooked basmati rice for evening distribution',
    CURRENT_DATE + INTERVAL '1 day',
    'Bengaluru',
    'available',
    '/images/rice.jpg',
    NOW() - INTERVAL '12 days'
FROM users WHERE email = 'donor@example.com';

-- Donation 3: Bread Loaves (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Bread Loaves',
    'VEG',
    '20 loaves',
    'Fresh bread from bakery surplus, whole wheat and multi-grain',
    CURRENT_DATE + INTERVAL '3 days',
    'Bengaluru',
    'available',
    '/images/bread.jpg',
    NOW() - INTERVAL '10 days'
FROM users WHERE email = 'donor@example.com';

-- Donation 4: Mixed Fruits (Claimed)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Mixed Fruits',
    'VEG',
    '8 kg',
    'Seasonal fruits - apples, bananas, oranges, and grapes',
    CURRENT_DATE + INTERVAL '4 days',
    'Bengaluru',
    'claimed',
    '/images/fruits.jpg',
    NOW() - INTERVAL '6 days'
FROM users WHERE email = 'donor@example.com';

-- Donation 5: Prepared Meals (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Prepared Meals',
    'NON-VEG',
    '30 meals',
    'Restaurant surplus meals including chicken biryani and curry',
    CURRENT_DATE + INTERVAL '1 day',
    'Bengaluru',
    'available',
    '/images/meals.jpg',
    NOW() - INTERVAL '3 days'
FROM users WHERE email = 'donor@example.com';

-- Donation 6: Packaged Snacks (Available) - Hotel donor
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Packaged Snacks',
    'VEG',
    '50 packets',
    'Sealed packaged snacks, biscuits, and chips from hotel buffet',
    CURRENT_DATE + INTERVAL '30 days',
    'Mumbai',
    'available',
    '/images/snacks.jpg',
    NOW() - INTERVAL '2 days'
FROM users WHERE email = 'hotel@example.com';

-- Donation 7: Dairy Products (Available) - Hotel donor
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Dairy Products',
    'VEG',
    '15 liters',
    'Fresh milk and yogurt from hotel kitchen surplus',
    CURRENT_DATE + INTERVAL '2 days',
    'Mumbai',
    'available',
    '/images/dairy.jpg',
    NOW() - INTERVAL '1 day'
FROM users WHERE email = 'hotel@example.com';

-- Donation 8: Canned Goods (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Canned Goods',
    'VEG',
    '40 cans',
    'Various canned vegetables, soups, and beans - all sealed',
    CURRENT_DATE + INTERVAL '365 days',
    'Bengaluru',
    'available',
    '/images/canned.jpg',
    NOW() - INTERVAL '5 days'
FROM users WHERE email = 'donor@example.com';

-- Donation 9: Fresh Salad (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Fresh Salad',
    'VEGAN',
    '12 portions',
    'Pre-made fresh salad portions with dressing packets',
    CURRENT_DATE + INTERVAL '1 day',
    'Mumbai',
    'available',
    '/images/salad.jpg',
    NOW()
FROM users WHERE email = 'hotel@example.com';

-- Donation 10: Pasta & Noodles (Available)
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT 
    id,
    'Pasta & Noodles',
    'VEG',
    '25 packets',
    'Unopened pasta and instant noodle packets',
    CURRENT_DATE + INTERVAL '90 days',
    'Bengaluru',
    'available',
    '/images/pasta.jpg',
    NOW() - INTERVAL '4 days'
FROM users WHERE email = 'donor@example.com';

-- =====================================================
-- STEP 3: INSERT SAMPLE REPORTS (Last 15 days)
-- =====================================================

INSERT INTO reports (report_date, total_donations, total_food_saved_kg, total_receivers_served) VALUES
(CURRENT_DATE - INTERVAL '14 days', 25, 135.7, 56),
(CURRENT_DATE - INTERVAL '13 days', 23, 120.2, 54),
(CURRENT_DATE - INTERVAL '12 days', 27, 145.9, 60),
(CURRENT_DATE - INTERVAL '11 days', 21, 110.5, 48),
(CURRENT_DATE - INTERVAL '10 days', 29, 155.3, 64),
(CURRENT_DATE - INTERVAL '9 days', 24, 128.1, 52),
(CURRENT_DATE - INTERVAL '8 days', 26, 139.4, 58),
(CURRENT_DATE - INTERVAL '7 days', 22, 115.8, 50),
(CURRENT_DATE - INTERVAL '6 days', 28, 150.6, 62),
(CURRENT_DATE - INTERVAL '5 days', 25, 133.2, 56),
(CURRENT_DATE - INTERVAL '4 days', 23, 122.7, 54),
(CURRENT_DATE - INTERVAL '3 days', 27, 144.5, 60),
(CURRENT_DATE - INTERVAL '2 days', 24, 130.9, 52),
(CURRENT_DATE - INTERVAL '1 day', 26, 140.3, 58),
(CURRENT_DATE, 22, 118.6, 48);

-- =====================================================
-- DATA INSERTION COMPLETE ✅
-- =====================================================
-- Login Credentials (All passwords: password123)
-- =====================================================
-- Admin:       admin@foodshare.com / password123
-- Donor 1:     donor@example.com / password123
-- Donor 2:     hotel@example.com / password123
-- Receiver 1:  receiver@example.com / password123
-- Receiver 2:  ngo@example.com / password123
-- =====================================================
