-- =====================================================
-- QUICK RUN - Copy and Run This Entire File
-- =====================================================
-- This combines BOTH init and data scripts
-- Run this ONCE in Supabase SQL Editor to set up everything
-- ⚠️ WARNING: This will DELETE all existing data!
-- =====================================================

-- =====================================================
-- PART 1: DROP & CREATE SCHEMA
-- =====================================================

-- Drop all existing tables
DROP TABLE IF EXISTS donation_requests CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS feedback CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS donations CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    city VARCHAR(100),
    role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_ADMIN', 'ROLE_DONOR', 'ROLE_RECEIVER')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create donations table
CREATE TABLE donations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    donor_id UUID NOT NULL,
    food_name VARCHAR(255) NOT NULL,
    food_type VARCHAR(50) NOT NULL CHECK (food_type IN ('VEG', 'NON-VEG', 'VEGAN')),
    quantity VARCHAR(100) NOT NULL,
    description TEXT,
    expiry_date DATE NOT NULL,
    city VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'available' CHECK (status IN ('available', 'claimed', 'expired', 'completed')),
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_donor FOREIGN KEY (donor_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create donation_requests table
CREATE TABLE donation_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    donation_id UUID NOT NULL,
    receiver_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected', 'completed')),
    request_date TIMESTAMP NOT NULL DEFAULT NOW(),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_donation FOREIGN KEY (donation_id) REFERENCES donations(id) ON DELETE CASCADE,
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create reports table
CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_date DATE NOT NULL UNIQUE,
    total_donations INTEGER NOT NULL DEFAULT 0,
    total_food_saved_kg DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_receivers_served INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create feedback table
CREATE TABLE feedback (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    donation_id UUID,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    comments TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_feedback_donation FOREIGN KEY (donation_id) REFERENCES donations(id) ON DELETE SET NULL
);

-- Create matches table
CREATE TABLE matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    donation_id UUID NOT NULL,
    receiver_id UUID NOT NULL,
    match_date TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(50) NOT NULL DEFAULT 'matched' CHECK (status IN ('matched', 'completed', 'cancelled')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_match_donation FOREIGN KEY (donation_id) REFERENCES donations(id) ON DELETE CASCADE,
    CONSTRAINT fk_match_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_donations_donor ON donations(donor_id);
CREATE INDEX idx_donations_status ON donations(status);
CREATE INDEX idx_donations_city ON donations(city);
CREATE INDEX idx_donation_requests_donation ON donation_requests(donation_id);
CREATE INDEX idx_donation_requests_receiver ON donation_requests(receiver_id);
CREATE INDEX idx_matches_donation ON matches(donation_id);
CREATE INDEX idx_matches_receiver ON matches(receiver_id);
CREATE INDEX idx_reports_date ON reports(report_date);

-- =====================================================
-- PART 2: INSERT DEMO DATA
-- =====================================================

-- Insert users (Password: password123 for all)
INSERT INTO users (name, email, password, phone, city, role) VALUES
('Admin User', 'admin@foodshare.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '1234567890', 'Chennai', 'ROLE_ADMIN'),
('Donor Demo', 'donor@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '9876543210', 'Bengaluru', 'ROLE_DONOR'),
('Receiver Demo', 'receiver@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '9876554321', 'Delhi', 'ROLE_RECEIVER'),
('Hotel Taj Manager', 'hotel@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '9123456789', 'Mumbai', 'ROLE_DONOR'),
('NGO Help Foundation', 'ngo@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2', '9234567890', 'Delhi', 'ROLE_RECEIVER');

-- Insert donations
INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Fresh Vegetables', 'VEG', '5 kg', 'Fresh organic vegetables from our farm', CURRENT_DATE + 2, 'Bengaluru', 'available', '/images/vegetables.jpg', NOW() - INTERVAL '15 days' FROM users WHERE email = 'donor@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Cooked Rice', 'VEG', '10 servings', 'Freshly cooked basmati rice', CURRENT_DATE + 1, 'Bengaluru', 'available', '/images/rice.jpg', NOW() - INTERVAL '12 days' FROM users WHERE email = 'donor@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Bread Loaves', 'VEG', '20 loaves', 'Fresh bakery surplus', CURRENT_DATE + 3, 'Bengaluru', 'available', '/images/bread.jpg', NOW() - INTERVAL '10 days' FROM users WHERE email = 'donor@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Mixed Fruits', 'VEG', '8 kg', 'Seasonal fruits variety', CURRENT_DATE + 4, 'Bengaluru', 'claimed', '/images/fruits.jpg', NOW() - INTERVAL '6 days' FROM users WHERE email = 'donor@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Prepared Meals', 'NON-VEG', '30 meals', 'Restaurant surplus meals', CURRENT_DATE + 1, 'Bengaluru', 'available', '/images/meals.jpg', NOW() - INTERVAL '3 days' FROM users WHERE email = 'donor@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Packaged Snacks', 'VEG', '50 packets', 'Sealed snacks from hotel', CURRENT_DATE + 30, 'Mumbai', 'available', '/images/snacks.jpg', NOW() - INTERVAL '2 days' FROM users WHERE email = 'hotel@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Dairy Products', 'VEG', '15 liters', 'Fresh milk and yogurt', CURRENT_DATE + 2, 'Mumbai', 'available', '/images/dairy.jpg', NOW() - INTERVAL '1 day' FROM users WHERE email = 'hotel@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Canned Goods', 'VEG', '40 cans', 'Sealed canned vegetables', CURRENT_DATE + 365, 'Bengaluru', 'available', '/images/canned.jpg', NOW() - INTERVAL '5 days' FROM users WHERE email = 'donor@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Fresh Salad', 'VEGAN', '12 portions', 'Pre-made fresh salad', CURRENT_DATE + 1, 'Mumbai', 'available', '/images/salad.jpg', NOW() FROM users WHERE email = 'hotel@example.com';

INSERT INTO donations (donor_id, food_name, food_type, quantity, description, expiry_date, city, status, image_url, created_at)
SELECT id, 'Pasta & Noodles', 'VEG', '25 packets', 'Unopened pasta packets', CURRENT_DATE + 90, 'Bengaluru', 'available', '/images/pasta.jpg', NOW() - INTERVAL '4 days' FROM users WHERE email = 'donor@example.com';

-- Insert reports (last 15 days)
INSERT INTO reports (report_date, total_donations, total_food_saved_kg, total_receivers_served) VALUES
(CURRENT_DATE - 14, 25, 135.7, 56),
(CURRENT_DATE - 13, 23, 120.2, 54),
(CURRENT_DATE - 12, 27, 145.9, 60),
(CURRENT_DATE - 11, 21, 110.5, 48),
(CURRENT_DATE - 10, 29, 155.3, 64),
(CURRENT_DATE - 9, 24, 128.1, 52),
(CURRENT_DATE - 8, 26, 139.4, 58),
(CURRENT_DATE - 7, 22, 115.8, 50),
(CURRENT_DATE - 6, 28, 150.6, 62),
(CURRENT_DATE - 5, 25, 133.2, 56),
(CURRENT_DATE - 4, 23, 122.7, 54),
(CURRENT_DATE - 3, 27, 144.5, 60),
(CURRENT_DATE - 2, 24, 130.9, 52),
(CURRENT_DATE - 1, 26, 140.3, 58),
(CURRENT_DATE, 22, 118.6, 48);

-- =====================================================
-- ✅ SETUP COMPLETE!
-- =====================================================
-- Login credentials (all passwords: password123):
--   admin@foodshare.com
--   donor@example.com
--   receiver@example.com
--   hotel@example.com
--   ngo@example.com
-- =====================================================
