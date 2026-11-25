CREATE TABLE service_types (
                               service_type_id UUID PRIMARY KEY,
                               service_name VARCHAR(100) NOT NULL UNIQUE,
                               description TEXT,
                               estimated_duration_minutes INTEGER NOT NULL,
                               price NUMERIC(10, 2) NOT NULL,
                               is_active BOOLEAN NOT NULL DEFAULT TRUE,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT chk_service_duration
                                   CHECK (estimated_duration_minutes > 0),

                               CONSTRAINT chk_service_price
                                   CHECK (price >= 0)
);