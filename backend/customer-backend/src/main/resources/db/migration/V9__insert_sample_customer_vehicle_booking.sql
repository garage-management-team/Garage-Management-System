DO $$
DECLARE
new_customer_id UUID;
    new_vehicle_id UUID;
BEGIN
    -- 1. Insert sample customer
INSERT INTO customers (
    first_name,
    last_name,
    email,
    phone,
    password_hash
)
VALUES (
           'Virusan',
           'Thavanathan',
           'virusan@gmail.com',
           '0771234567',
           'SAMPLE_HASH_NOT_FOR_LOGIN'
       )
    RETURNING customer_id INTO new_customer_id;


-- 2. Insert sample vehicle using the customer ID
INSERT INTO vehicles (
    customer_id,
    registration_number,
    brand,
    model,
    manufacture_year,
    vehicle_type,
    color
)
VALUES (
           new_customer_id,
           'ABC-1234',
           'Toyota',
           'Aqua',
           2020,
           'CAR',
           'White'
       )
    RETURNING vehicle_id INTO new_vehicle_id;


-- 3. Insert sample booking using customer and vehicle IDs
INSERT INTO bookings (
    customer_id,
    vehicle_id,
    service_id,
    booking_date,
    start_time,
    end_time,
    status,
    customer_note,
    service_name
)
VALUES (
           new_customer_id,
           new_vehicle_id,
           'b166c4e5-1534-4073-87e6-3fa5b342ea47',
           '2026-11-27',
           '09:00:00',
           '10:00:00',
           'PROCESSING',
           'Please check the engine and brakes.',
           'Full Service'
       );

END $$;