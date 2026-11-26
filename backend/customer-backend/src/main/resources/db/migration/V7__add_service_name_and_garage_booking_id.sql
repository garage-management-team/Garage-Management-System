-- Add service name snapshot
ALTER TABLE bookings
ADD COLUMN service_name VARCHAR(150);

-- Add Garage Backend booking reference
ALTER TABLE bookings
ADD COLUMN garage_booking_id UUID UNIQUE;