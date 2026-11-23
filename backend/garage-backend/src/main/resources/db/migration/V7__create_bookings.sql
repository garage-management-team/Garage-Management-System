CREATE TABLE bookings (
                          booking_id UUID PRIMARY KEY,
                          slot_id UUID NOT NULL,
                          customer_id UUID NOT NULL,
                          vehicle_id UUID NOT NULL,
                          booking_status VARCHAR(30) NOT NULL,
                          rejection_reason TEXT,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_bookings_slot
                              FOREIGN KEY (slot_id)
                                  REFERENCES service_slots(slot_id),

                          CONSTRAINT chk_booking_status
                              CHECK (
                                  booking_status IN (
                                                     'PENDING',
                                                     'CONFIRMED',
                                                     'REJECTED'
                                      )
                                  )
);