CREATE TABLE service_slots (
                               slot_id UUID PRIMARY KEY,
                               service_type_id UUID NOT NULL,
                               created_by UUID NOT NULL,
                               slot_date DATE NOT NULL,
                               start_time TIME NOT NULL,
                               end_time TIME NOT NULL,
                               vehicle_capacity INTEGER NOT NULL,
                               is_active BOOLEAN NOT NULL DEFAULT TRUE,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_service_slots_service_type
                                   FOREIGN KEY (service_type_id)
                                       REFERENCES service_types(service_type_id),

                               CONSTRAINT fk_service_slots_manager
                                   FOREIGN KEY (created_by)
                                       REFERENCES garage_managers(personnel_id),

                               CONSTRAINT chk_slot_capacity
                                   CHECK (vehicle_capacity > 0),

                               CONSTRAINT chk_slot_time
                                   CHECK (end_time > start_time)
);