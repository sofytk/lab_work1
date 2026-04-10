-- Таблица работников
CREATE TABLE person
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name   VARCHAR(255),
    external_id UUID UNIQUE,
    age         SMALLINT CHECK ( age BETWEEN 16 AND 75)
);
-- Таблица проектов
CREATE TABLE project
(
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_name VARCHAR(255),
    description  TEXT,
    tech_stack   TEXT[]
);
-- Таблица  информацией о работнике в команде
CREATE TABLE project_member_information
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id  INT REFERENCES person (id),
    project_id INT REFERENCES project (id),
    role       VARCHAR(100),
    salary     INT,
    bonus      INT,
    skills     TEXT[]
);
