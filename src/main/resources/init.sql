-- Enable pgcrypto for UUID generation
create extension if not exists "pgcrypto";

create table if not exists users (
  id uuid primary key default gen_random_uuid(),
  name text not null,
  email text unique not null,
  password text not null,
  role text check (role in ('admin','donor','receiver')) not null,
  city text,
  phone text,
  created_at timestamptz default now()
);

create table if not exists donation (
  id uuid primary key default gen_random_uuid(),
  donor_id uuid references users(id) on delete cascade,
  food_name text not null,
  description text,
  quantity text,
  food_type text check (food_type in ('veg','non-veg','other')),
  expiry_date date,
  image_url text,
  city text,
  status text default 'available' check (status in ('available','claimed','expired')),
  created_at timestamptz default now()
);

create table if not exists receiver_request (
  id uuid primary key default gen_random_uuid(),
  receiver_id uuid references users(id) on delete cascade,
  donation_id uuid references donation(id) on delete cascade,
  status text default 'pending' check (status in ('pending','approved','completed')),
  requested_at timestamptz default now()
);

create table if not exists feedback (
  id uuid primary key default gen_random_uuid(),
  donor_id uuid references users(id),
  receiver_id uuid references users(id),
  donation_id uuid references donation(id),
  rating int check (rating between 1 and 5),
  comments text,
  created_at timestamptz default now()
);

create table if not exists reports (
  id uuid primary key default gen_random_uuid(),
  total_donations int default 0,
  total_food_saved_kg numeric(8,2) default 0,
  total_receivers_served int default 0,
  report_date date default current_date
);

create table if not exists match (
  id uuid primary key default gen_random_uuid(),
  donation_id uuid references donation(id),
  receiver_id uuid references users(id),
  matched_at timestamptz default now()
);

-- Sample users
insert into users(id, name, email, password, role, city, phone, created_at) values
  (gen_random_uuid(),'Admin','admin@foodshare.com','$2a$10$t.jSU/PXikFjcIhU2TSOEOGZl1J0i4y3aLjxj7mFCCycXsx2ce4JW','admin','Chennai','1234567890', now()),
  (gen_random_uuid(),'Donor Demo','donor@example.com','$2a$10$jXwzUZWhnQLeQoqHuvsLGe1ITEPrf4pRcgkl5cr0E5FfKx4/5GEl.','donor','Bengaluru','9876543210', now()),
  (gen_random_uuid(),'Receiver Demo','receiver@example.com','$2a$10$mgohx2Ugz/u/ftJFholPvenwCzn5PYzPifgWITp2z2g4.K4KeNE/q','receiver','Delhi','9876554321', now())
ON CONFLICT (email) DO NOTHING;

-- Sample donations
insert into donation(id, donor_id, food_name, description, quantity, food_type, expiry_date, image_url, city, status, created_at)
select gen_random_uuid(), (select id from users where email='donor@example.com'), 'Cooked Rice','Leftover from buffet','5 Plates','veg', CURRENT_DATE + INTERVAL '2 days','/images/sample1.jpg','Chennai','available',now()
union
select gen_random_uuid(), (select id from users where email='donor@example.com'), 'Bread Loaves','Fresh bakery loaves','10 Loaves','veg', CURRENT_DATE + INTERVAL '1 days','/images/sample2.jpg','Bengaluru','available',now()
union
select gen_random_uuid(), (select id from users where email='donor@example.com'), 'Chicken Curry','Spicy and packed','3 Packs','non-veg', CURRENT_DATE + INTERVAL '2 days','/images/sample3.jpg','Delhi','available',now();
