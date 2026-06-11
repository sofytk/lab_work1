-- Таблица warehouse_person
CREATE TABLE warehouse_person
(
    id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    age       SMALLINT,
    skills    TEXT[],
    salary    INT,
    bonus     INT

);

-- Таблица project_member_information
CREATE TABLE project_member_information
(
    person_id  INT NOT NULL REFERENCES warehouse_person (id) ON DELETE CASCADE,
    project_id INT NOT NULL,
    role       VARCHAR(100),
    PRIMARY KEY (person_id, project_id)
);

-- Таблица dim_project
CREATE TABLE dim_project
(
    project_id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_name VARCHAR(255),
    description  TEXT,
    tech_stack   TEXT[]
);

CREATE INDEX idx_warehouse_person_full_name ON warehouse_person (full_name);
CREATE INDEX idx_project_member_info_person ON project_member_information (person_id);
CREATE INDEX idx_project_member_info_project ON project_member_information (project_id);

