CREATE TABLE garage_managers (
                                 personnel_id UUID PRIMARY KEY,
                                 password_hash VARCHAR(255) NOT NULL,

                                 CONSTRAINT fk_garage_managers_personnel
                                     FOREIGN KEY (personnel_id)
                                         REFERENCES garage_personnel(personnel_id)
);