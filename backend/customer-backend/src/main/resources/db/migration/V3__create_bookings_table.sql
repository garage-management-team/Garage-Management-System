CREATE TABLE bookings (
                          booking_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          customer_id UUID NOT NULL,
                          vehicle_id UUID NOT NULL,
                          service_id UUID NOT NULL,

                          booking_date DATE NOT NULL,
                          start_time TIME NOT NULL,
                          end_time TIME NOT NULL,

                          status VARCHAR(30) NOT NULL DEFAULT 'PROCESSING',
                          customer_note TEXT,
                          rejection_reason TEXT,
                          cancellation_reason TEXT,

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_bookings_customer
                              FOREIGN KEY (customer_id)
                                  REFERENCES customers(customer_id)
                                  ON DELETE RESTRICT,

                          CONSTRAINT fk_bookings_vehicle
                              FOREIGN KEY (vehicle_id)
                                  REFERENCES vehicles(vehicle_id)
                                  ON DELETE RESTRICT,

                          CONSTRAINT chk_booking_time
                              CHECK (end_time > start_time),

                          CONSTRAINT chk_booking_status
                              CHECK (status IN (
                                                'PROCESSING',
                                                'PENDING',
                                                'CONFIRMED',
                                                'REJECTED',
                                                'CANCELLED',
                                                'CHECKED_IN',
                                                'IN_PROGRESS',
                                                'COMPLETED'
                                  ))
);

CREATE INDEX idx_bookings_customer_id ON bookings(customer_id);
CREATE INDEX idx_bookings_vehicle_id ON bookings(vehicle_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_date ON bookings(booking_date);
CREATE INDEX idx_bookings_customer_date ON bookings(customer_id, booking_date);