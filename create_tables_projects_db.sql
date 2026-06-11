-- Таблица работников проекта
CREATE TABLE person
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name   VARCHAR(255) NOT NULL,
    age         SMALLINT CHECK ( age BETWEEN 16 AND 75)
);

-- Таблица проектов
CREATE TABLE project
(
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_name    VARCHAR(255) NOT NULL,
    description     TEXT
);

-- Таблица технологий проекта
CREATE TABLE project_tech_stack
(
    project_id INT NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    technology VARCHAR(100) NOT NULL,
    PRIMARY KEY (project_id, technology)
);

-- Таблица членов проекта с составным ключом
CREATE TABLE project_member
(
    person_id  INT NOT NULL REFERENCES person (id) ON DELETE CASCADE,
    project_id INT NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    role_name  VARCHAR(100),
    PRIMARY KEY (person_id, project_id)
);

-- Создаем индекс для оптимизации поиска
CREATE INDEX idx_project_member_person ON project_member(person_id);
CREATE INDEX idx_project_tech_stack_project ON project_tech_stack(project_id);
CREATE INDEX idx_project_member_project ON project_member(project_id);
CREATE INDEX idx_project_tech_stack ON project_tech_stack(project_id);

INSERT INTO person (full_name, age) VALUES
                                        ('Иванов Иван Иванович', 35),
                                        ('Петров Петр Петрович', 42),
                                        ('Сидорова Анна Сергеевна', 28),
                                        ('Козлов Дмитрий', 31),
                                        ('Соколова Елена', 27),
                                        ('Морозов А.А.', 45),
                                        ('Волков В.', 38),
                                        ('Новикова Н.Н.', 33),
                                        ('Кузнецов К К', 29),

                                        ('Саша Петров', 25),
                                        ('Паша Иванов', 26),
                                        ('Дима Сидоров', 30),
                                        ('Женя Соколов', 32),
                                        ('Гоша Козлов', 27),
                                        ('Тёма Морозов', 24),

                                        ('  Иванов   Иван   Иванович  ', 35),
                                        ('Алексей Егоров Евгеньевич', 40),
                                        ('Ольга Смирнова Владимировна', 37),
                                        ('Михайлов М. Сергеевич', 41),
                                        ('Андреев Андрей А.', 39),
                                        ('Владимиров В.Владимирович', 44),
                                        ('Ёлкин Артём Сергеевич', 23),
                                        ('елкин артем сергеевич', 24),

                                        ('Иван Иванов', 35),
                                        ('Петр Петров', 22),

                                        ('Простой', 22);

SELECT * FROM person;

INSERT INTO project (project_name, description) VALUES
                                                    ('Автоматизация склада', 'Разработка WMS для оптимизации складских операций'),
                                                    ('CRM система', 'Клиентская база и управление продажами'),
                                                    ('Мобильное приложение доставки', 'Приложение для курьеров и клиентов'),
                                                    ('Аналитический дашборд', 'BI-система для руководства'),
                                                    ('ETL платформа', 'Интеграция данных и построение хранилища');

INSERT INTO project_tech_stack (project_id, technology) VALUES
                                                            (1, 'Java'),
                                                            (1, 'Spring Boot'),
                                                            (1, 'PostgreSQL'),
                                                            (1, 'Hibernate'),
                                                            (1, 'Docker');

INSERT INTO project_tech_stack (project_id, technology) VALUES
                                                            (2, 'React'),
                                                            (2, 'Node.js'),
                                                            (2, 'MongoDB'),
                                                            (2, 'Express'),
                                                            (2, 'TypeScript');

INSERT INTO project_tech_stack (project_id, technology) VALUES
                                                            (3, 'Flutter'),
                                                            (3, 'Dart'),
                                                            (3, 'Firebase'),
                                                            (3, 'Node.js');

INSERT INTO project_tech_stack (project_id, technology) VALUES
                                                            (4, 'Python'),
                                                            (4, 'Pandas'),
                                                            (4, 'Tableau'),
                                                            (4, 'SQL'),
                                                            (4, 'Airflow');

INSERT INTO project_tech_stack (project_id, technology) VALUES
                                                            (5, 'Apache Spark'),
                                                            (5, 'Kafka'),
                                                            (5, 'Scala'),
                                                            (5, 'Airflow'),
                                                            (5, 'Docker'),
                                                            (5, 'Kubernetes');

INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (1, 1, 'Team Lead'),
                                                                  (2, 1, 'Senior Developer'),
                                                                  (3, 1, 'QA Engineer'),
                                                                  (4, 1, 'Junior Developer');

INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (5, 2, 'Frontend Lead'),
                                                                  (6, 2, 'Backend Developer'),
                                                                  (7, 2, 'UI/UX Designer');

INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (8, 3, 'Mobile Developer'),
                                                                  (9, 3, 'Mobile Developer'),
                                                                  (10, 3, 'Backend Developer'),
                                                                  (11, 3, 'QA Engineer');

INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (12, 4, 'Data Analyst'),
                                                                  (13, 4, 'Data Engineer'),
                                                                  (14, 4, 'BI Developer'),
                                                                  (15, 4, 'Team Lead');

INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (16, 5, 'Data Engineer'),
                                                                  (17, 5, 'Data Architect'),
                                                                  (18, 5, 'DevOps'),
                                                                  (19, 5, 'Junior Data Engineer');

INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (20, 1, 'DevOps'),
                                                                  (21, 2, 'Fullstack Developer'),
                                                                  (22, 4, 'Data Scientist'),
                                                                  (23, 5, 'Data Analyst'),
                                                                  (24, 3, 'Project Manager'),
                                                                  (25, 1, 'Intern');

