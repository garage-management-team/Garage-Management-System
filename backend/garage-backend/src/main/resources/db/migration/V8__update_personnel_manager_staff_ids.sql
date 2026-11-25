-- Remove existing foreign keys before changing referenced IDs
ALTER TABLE service_slots
DROP CONSTRAINT fk_service_slots_manager;

ALTER TABLE slot_staff_assignments
DROP CONSTRAINT fk_slot_staff_staff;


-- Add independent IDs for managers and staff
ALTER TABLE garage_managers
    ADD COLUMN manager_id UUID;

ALTER TABLE garage_staff
    ADD COLUMN staff_id UUID;


-- Generate UUIDs for existing records
UPDATE garage_managers
SET manager_id = gen_random_uuid();

UPDATE garage_staff
SET staff_id = gen_random_uuid();


-- Update existing references to use the new IDs
UPDATE service_slots ss
SET created_by = gm.manager_id
    FROM garage_managers gm
WHERE ss.created_by = gm.personnel_id;

UPDATE slot_staff_assignments ssa
SET staff_id = gs.staff_id
    FROM garage_staff gs
WHERE ssa.staff_id = gs.personnel_id;


-- Make the new IDs mandatory
ALTER TABLE garage_managers
    ALTER COLUMN manager_id SET NOT NULL;

ALTER TABLE garage_staff
    ALTER COLUMN staff_id SET NOT NULL;


-- Replace the existing primary keys
ALTER TABLE garage_managers
DROP CONSTRAINT garage_managers_pkey;

ALTER TABLE garage_staff
DROP CONSTRAINT garage_staff_pkey;

ALTER TABLE garage_managers
    ADD CONSTRAINT garage_managers_pkey PRIMARY KEY (manager_id);

ALTER TABLE garage_staff
    ADD CONSTRAINT garage_staff_pkey PRIMARY KEY (staff_id);


-- Ensure each personnel record has at most one manager/staff record
ALTER TABLE garage_managers
    ADD CONSTRAINT uq_garage_managers_personnel UNIQUE (personnel_id);

ALTER TABLE garage_staff
    ADD CONSTRAINT uq_garage_staff_personnel UNIQUE (personnel_id);


-- Create updated foreign keys
ALTER TABLE service_slots
    ADD CONSTRAINT fk_service_slots_manager
        FOREIGN KEY (created_by)
            REFERENCES garage_managers(manager_id);

ALTER TABLE slot_staff_assignments
    ADD CONSTRAINT fk_slot_staff_staff
        FOREIGN KEY (staff_id)
            REFERENCES garage_staff(staff_id);