CREATE TABLE garage_staff (
                              personnel_id UUID PRIMARY KEY,
                              specialization VARCHAR(100),

                              CONSTRAINT fk_garage_staff_personnel
                                  FOREIGN KEY (personnel_id)
                                      REFERENCES garage_personnel(personnel_id)
);