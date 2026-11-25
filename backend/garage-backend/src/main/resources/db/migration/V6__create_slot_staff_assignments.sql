CREATE TABLE slot_staff_assignments (
                                        assignment_id UUID PRIMARY KEY,
                                        slot_id UUID NOT NULL,
                                        staff_id UUID NOT NULL,
                                        assigned_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                        CONSTRAINT fk_slot_staff_slot
                                            FOREIGN KEY (slot_id)
                                                REFERENCES service_slots(slot_id),

                                        CONSTRAINT fk_slot_staff_staff
                                            FOREIGN KEY (staff_id)
                                                REFERENCES garage_staff(personnel_id),

                                        CONSTRAINT uq_slot_staff_assignment
                                            UNIQUE (slot_id, staff_id)
);