ALTER TABLE bookings
DROP CONSTRAINT chk_booking_status;

ALTER TABLE bookings
    ADD CONSTRAINT chk_booking_status
        CHECK (status IN ('PROCESSING', 'PENDING', 'ACCEPT', 'REJECT'));