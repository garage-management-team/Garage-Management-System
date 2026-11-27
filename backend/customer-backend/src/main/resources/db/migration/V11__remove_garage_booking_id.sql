-- Remove the separate Garage Backend booking ID
ALTER TABLE bookings
DROP COLUMN IF EXISTS garage_booking_id;