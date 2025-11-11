-- =====================================================
-- CLEAN INIT.SQL - Database Schema Initialization
-- =====================================================
-- Run this FIRST in Supabase SQL Editor
-- This will create all tables from scratch
-- =====================================================

-- Drop all existing tables (CAREFUL: This deletes all data!)
DROP TABLE IF EXISTS donation_requests CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS feedback CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS donations CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =====================================================
-- CREATE USERS TABLE
-- =====================================================
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

-- =====================================================
-- CREATE DONATIONS TABLE
-- =====================================================
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

-- =====================================================
-- CREATE DONATION_REQUESTS TABLE
-- =====================================================
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

-- =====================================================
-- CREATE REPORTS TABLE
-- =====================================================
CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_date DATE NOT NULL UNIQUE,
    total_donations INTEGER NOT NULL DEFAULT 0,
    total_food_saved_kg DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_receivers_served INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- =====================================================
-- CREATE FEEDBACK TABLE
-- =====================================================
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

-- =====================================================
-- CREATE MATCHES TABLE
-- =====================================================
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

-- =====================================================
-- CREATE INDEXES
-- =====================================================
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
-- SCHEMA CREATION COMPLETE ✅
-- =====================================================
-- Next: Run CLEAN_DATA.sql to insert demo data
-- =====================================================
