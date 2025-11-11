-- =====================================================
-- FORCE CLEANUP - Remove ALL Old Tables
-- =====================================================
-- Run this FIRST to completely clean your database
-- Then run CLEAN_INIT.sql
-- Then run CLEAN_DATA.sql
-- =====================================================

-- Drop ALL old tables (both old and new naming)
DROP TABLE IF EXISTS donation_requests CASCADE;
DROP TABLE IF EXISTS receiver_request CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS match CASCADE;
DROP TABLE IF EXISTS feedback CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS donations CASCADE;
DROP TABLE IF EXISTS donation CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Verify all tables are gone
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
  AND table_type = 'BASE TABLE'
ORDER BY table_name;

-- =====================================================
-- Result should be EMPTY (no tables)
-- After this, run CLEAN_INIT.sql to create fresh schema
-- =====================================================
