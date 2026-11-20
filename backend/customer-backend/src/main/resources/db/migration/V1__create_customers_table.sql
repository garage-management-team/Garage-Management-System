CREATE TABLE customers (
                           customer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           first_name VARCHAR(100) NOT NULL,
                           last_name VARCHAR(100) NOT NULL,

                           email VARCHAR(255) NOT NULL UNIQUE,
                           phone VARCHAR(20),

                           password_hash VARCHAR(255) NOT NULL,


                           updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
