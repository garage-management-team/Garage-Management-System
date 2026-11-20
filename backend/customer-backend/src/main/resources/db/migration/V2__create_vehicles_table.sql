CREATE TABLE vehicles (
                          vehicle_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          customer_id UUID NOT NULL,
                          registration_number VARCHAR(20) NOT NULL UNIQUE,
                          brand VARCHAR(100) NOT NULL,
                          model VARCHAR(100) NOT NULL,
                          manufacture_year INTEGER NOT NULL,
                          vehicle_type VARCHAR(30) NOT NULL,

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_vehicles_customer
                              FOREIGN KEY (customer_id)
                                  REFERENCES customers(customer_id)
                                  ON DELETE CASCADE,

                          CONSTRAINT chk_vehicle_year
                              CHECK (manufacture_year BETWEEN 1900 AND 2100),

                          CONSTRAINT chk_vehicle_type
                              CHECK (vehicle_type IN (
                                                      'CAR',
                                                      'SUV',
                                                      'VAN',
                                                      'MOTORCYCLE',
                                                      'THREE_WHEELER',
                                                      'TRUCK'
                                  ))
);

CREATE INDEX idx_vehicles_customer_id ON vehicles(customer_id);