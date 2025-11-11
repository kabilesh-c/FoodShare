-- Insert Demo Users
-- Password for all users: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2

-- Admin User
INSERT INTO users (id, name, email, password, phone, city, role, created_at)
VALUES (
    gen_random_uuid(),
    'Admin User',
    'admin@foodshare.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2',
    '1234567890',
    'Chennai',
    'ROLE_ADMIN',
    NOW()
) ON CONFLICT (email) DO NOTHING;

-- Donor User
INSERT INTO users (id, name, email, password, phone, city, role, created_at)
VALUES (
    gen_random_uuid(),
    'Donor Demo',
    'donor@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2',
    '9876543210',
    'Bengaluru',
    'ROLE_DONOR',
    NOW()
) ON CONFLICT (email) DO NOTHING;

-- Receiver User
INSERT INTO users (id, name, email, password, phone, city, role, created_at)
VALUES (
    gen_random_uuid(),
    'Receiver Demo',
    'receiver@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2',
    '9876554321',
    'Delhi',
    'ROLE_RECEIVER',
    NOW()
) ON CONFLICT (email) DO NOTHING;

-- Note: Run this SQL script directly in your Supabase SQL editor
-- After running this, you can login with:
--   admin@foodshare.com / password123
--   donor@example.com / password123
--   receiver@example.com / password123
