-- Таблица person
CREATE TABLE person
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name  VARCHAR(255) NOT NULL,
    age        SMALLINT CHECK (age BETWEEN 16 AND 75)
);

-- Таблица skill
CREATE TABLE skill
(
    id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица resume
CREATE TABLE resume
(
    id               INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id        INT NOT NULL UNIQUE REFERENCES person (id) ON DELETE CASCADE,
    experience_years INT DEFAULT 0 CHECK (experience_years >= 0 AND experience_years <= 100)
);

-- Таблица resume_skill
CREATE TABLE resume_skill
(
    resume_id  INT NOT NULL REFERENCES resume (id) ON DELETE CASCADE,
    skill_id   INT NOT NULL REFERENCES skill (id) ON DELETE CASCADE,
    level      VARCHAR(50) CHECK (level IN ('beginner', 'intermediate', 'advanced', 'expert')),
    PRIMARY KEY (resume_id, skill_id)
);

-- Таблица salary
CREATE TABLE salary
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id  INT NOT NULL UNIQUE REFERENCES person (id) ON DELETE CASCADE,
    salary     INT CHECK (salary >= 0),
    bonus      INT CHECK (bonus >= 0),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_resume_person ON resume(person_id);
CREATE INDEX idx_resume_skill_resume ON resume_skill(resume_id);
CREATE INDEX idx_resume_skill_skill ON resume_skill(skill_id);
CREATE INDEX idx_salary_person ON salary(person_id);

INSERT INTO person (full_name, age) VALUES
                                        ('Иванов Иван Иванович', 35),
                                        ('Петров Петр Петрович', 42),
                                        ('Сидорова Анна Сергеевна', 28),
                                        ('Козлов Дмитрий', 31),
                                        ('Соколова Елена', 27),
                                        ('Морозов Александр Александрович', 45),
                                        ('Волков Владимир', 38),
                                        ('Новикова Наталья Николаевна', 33),
                                        ('Кузнецов Константин Константинович', 29),
                                        ('Саша Петров', 25),
                                        ('Паша Иванов', 26),
                                        ('Дима Сидоров', 30),
                                        ('Женя Соколов', 32),
                                        ('Гоша Козлов', 27),
                                        ('Тёма Морозов', 24),
                                        ('Коля Волков', 29),
                                        ('Маша Новикова', 26),
                                        ('Наташа Кузнецова', 31),
                                        ('Серёжа Попов', 28),
                                        ('Вова Лебедев', 34),
                                        ('Лёня Соловьев', 36),
                                        ('Юра Медведев', 33),
                                        ('  Иванов   Иван   Иванович  ', 35),
                                        ('Алексей Егоров Евгеньевич', 40),
                                        ('Ольга Смирнова Владимировна', 37),
                                        ('Михайлов М. Сергеевич', 41),
                                        ('Андреев Андрей А.', 39),
                                        ('Владимиров В.Владимирович', 44),
                                        ('Ёлкин Артём Сергеевич', 23),
                                        ('елкин артем сергеевич', 24),
                                        ('Иван Иванов', 35),
                                        ('Петр Петров', 22);

INSERT INTO skill (name) VALUES
                             ('Java'), ('Spring Boot'), ('PostgreSQL'), ('Hibernate'), ('Python'),
                             ('Django'), ('React'), ('Node.js'), ('Docker'), ('Kubernetes'),
                             ('Kotlin'), ('Flutter'), ('Apache Spark'), ('Kafka'), ('Selenium'),
                             ('AWS'), ('TypeScript'), ('Git'), ('Agile'), ('Scrum');

INSERT INTO skill (name) VALUES ('Postman');
INSERT INTO skill (name) VALUES ('Dart');
INSERT INTO skill (name) VALUES ('Android');
INSERT INTO skill (name) VALUES ('Pandas');

INSERT INTO resume (person_id,  experience_years) VALUES
                                                      (1, 12),
                                                      (2,  15),
                                                      (3, 6),
                                                      (4,  5),
                                                      (5,  7),
                                                      (6,  8),
                                                      (7,  6),
                                                      (8,  5),
                                                      (9,  4),
                                                      (10,  7),
                                                      (11,  9),
                                                      (12,  4),
                                                      (13,  3),
                                                      (14,  8),
                                                      (15,  10),
                                                      (16,  1),
                                                      (17, 8),
                                                      (18,  5),
                                                      (19,  6),
                                                      (20,  4),
                                                      (21,  7),
                                                      (22,  3),
                                                      (23,  9),
                                                      (24,  6),
                                                      (25,  10),
                                                      (26,  7),
                                                      (27,  5),
                                                      (28,  4),
                                                      (29,  3),
                                                      (30,  4),
                                                      (31,  1),
                                                      (32,  0);

WITH skill_ids AS (
    SELECT name, id FROM skill
)
INSERT INTO resume_skill (resume_id, skill_id, level) VALUES
(1, (SELECT id FROM skill_ids WHERE name='Java'), 'expert'),
(1, (SELECT id FROM skill_ids WHERE name='Spring Boot'), 'expert'),
(1, (SELECT id FROM skill_ids WHERE name='PostgreSQL'), 'advanced'),
(1, (SELECT id FROM skill_ids WHERE name='Docker'), 'advanced'),
(2, (SELECT id FROM skill_ids WHERE name='Java'), 'expert'),
(2, (SELECT id FROM skill_ids WHERE name='Spring Boot'), 'advanced'),
(2, (SELECT id FROM skill_ids WHERE name='Agile'), 'expert'),
(2, (SELECT id FROM skill_ids WHERE name='Scrum'), 'expert'),
(3, (SELECT id FROM skill_ids WHERE name='Selenium'), 'advanced'),
(3, (SELECT id FROM skill_ids WHERE name='Postman'), 'advanced'),
(4, (SELECT id FROM skill_ids WHERE name='Kotlin'), 'advanced'),
(4, (SELECT id FROM skill_ids WHERE name='Java'), 'intermediate'),
(4, (SELECT id FROM skill_ids WHERE name='PostgreSQL'), 'advanced'),
(5, (SELECT id FROM skill_ids WHERE name='React'), 'advanced'),
(5, (SELECT id FROM skill_ids WHERE name='TypeScript'), 'advanced'),
(6, (SELECT id FROM skill_ids WHERE name='Python'), 'advanced'),
(6, (SELECT id FROM skill_ids WHERE name='Django'), 'advanced'),
(6, (SELECT id FROM skill_ids WHERE name='React'), 'intermediate'),
(7, (SELECT id FROM skill_ids WHERE name='Docker'), 'expert'),
(7, (SELECT id FROM skill_ids WHERE name='Kubernetes'), 'advanced'),
(7, (SELECT id FROM skill_ids WHERE name='AWS'), 'advanced'),
(8, (SELECT id FROM skill_ids WHERE name='Python'), 'advanced'),
(8, (SELECT id FROM skill_ids WHERE name='Apache Spark'), 'advanced'),
(8, (SELECT id FROM skill_ids WHERE name='Kafka'), 'intermediate'),
(9, (SELECT id FROM skill_ids WHERE name='Flutter'), 'advanced'),
(9, (SELECT id FROM skill_ids WHERE name='Dart'), 'advanced'),
(10, (SELECT id FROM skill_ids WHERE name='React'), 'expert'),
(10, (SELECT id FROM skill_ids WHERE name='TypeScript'), 'expert'),
(10, (SELECT id FROM skill_ids WHERE name='Node.js'), 'intermediate'),
(11, (SELECT id FROM skill_ids WHERE name='Java'), 'advanced'),
(11, (SELECT id FROM skill_ids WHERE name='Spring Boot'), 'advanced'),
(11, (SELECT id FROM skill_ids WHERE name='Kafka'), 'intermediate'),
(12, (SELECT id FROM skill_ids WHERE name='Kotlin'), 'advanced'),
(12, (SELECT id FROM skill_ids WHERE name='Android'), 'advanced'),
(13, (SELECT id FROM skill_ids WHERE name='Python'), 'advanced'),
(13, (SELECT id FROM skill_ids WHERE name='Pandas'), 'advanced'),
(14, (SELECT id FROM skill_ids WHERE name='Agile'), 'expert'),
(14, (SELECT id FROM skill_ids WHERE name='Scrum'), 'expert'),
(15, (SELECT id FROM skill_ids WHERE name='Java'), 'expert'),
(15, (SELECT id FROM skill_ids WHERE name='AWS'), 'advanced'),
(16, (SELECT id FROM skill_ids WHERE name='Java'), 'beginner'),
(16, (SELECT id FROM skill_ids WHERE name='Git'), 'intermediate'),
(17, (SELECT id FROM skill_ids WHERE name='Selenium'), 'expert'),
(18, (SELECT id FROM skill_ids WHERE name='React'), 'advanced'),
(18, (SELECT id FROM skill_ids WHERE name='Node.js'), 'advanced'),
(19, (SELECT id FROM skill_ids WHERE name='Docker'), 'advanced'),
(19, (SELECT id FROM skill_ids WHERE name='Kubernetes'), 'intermediate'),
(20, (SELECT id FROM skill_ids WHERE name='Git'), 'advanced'),
(21, (SELECT id FROM skill_ids WHERE name='Agile'), 'expert'),
(21, (SELECT id FROM skill_ids WHERE name='Scrum'), 'expert'),
(22, (SELECT id FROM skill_ids WHERE name='PostgreSQL'), 'intermediate'),
(23, (SELECT id FROM skill_ids WHERE name='Python'), 'expert'),
(23, (SELECT id FROM skill_ids WHERE name='Django'), 'expert'),
(23, (SELECT id FROM skill_ids WHERE name='PostgreSQL'), 'advanced'),
(24, (SELECT id FROM skill_ids WHERE name='React'), 'advanced'),
(25, (SELECT id FROM skill_ids WHERE name='PostgreSQL'), 'expert'),
(26, (SELECT id FROM skill_ids WHERE name='AWS'), 'advanced'),
(27, (SELECT id FROM skill_ids WHERE name='Kotlin'), 'advanced'),
(27, (SELECT id FROM skill_ids WHERE name='Android'), 'advanced'),
(28, (SELECT id FROM skill_ids WHERE name='Selenium'), 'advanced'),
(29, (SELECT id FROM skill_ids WHERE name='Python'), 'intermediate'),
(29, (SELECT id FROM skill_ids WHERE name='Pandas'), 'intermediate'),
(29, (SELECT id FROM skill_ids WHERE name='PostgreSQL'), 'intermediate'),
(30, (SELECT id FROM skill_ids WHERE name='TypeScript'), 'advanced'),
(30, (SELECT id FROM skill_ids WHERE name='React'), 'intermediate'),
(30, (SELECT id FROM skill_ids WHERE name='Node.js'), 'intermediate'),
(31, (SELECT id FROM skill_ids WHERE name='Python'), 'beginner'),
(31, (SELECT id FROM skill_ids WHERE name='Git'), 'beginner'),
(32, (SELECT id FROM skill_ids WHERE name='Git'), 'beginner');

SELECT * from resume;

INSERT INTO salary (person_id, salary, bonus) VALUES
                                                  (1, 300000, 60000), (2, 250000, 50000), (3, 150000, 25000),
                                                  (4, 180000, 30000), (5, 200000, 35000), (6, 220000, 40000),
                                                  (7, 210000, 40000), (8, 190000, 35000), (9, 170000, 28000),
                                                  (10, 210000, 40000), (11, 230000, 45000), (12, 160000, 25000),
                                                  (13, 190000, 35000), (14, 240000, 50000), (15, 280000, 60000),
                                                  (16, 90000, 10000), (17, 180000, 30000), (18, 170000, 28000),
                                                  (19, 200000, 35000), (20, 150000, 20000), (21, 220000, 45000),
                                                  (22, 160000, 25000), (23, 200000, 40000), (24, 180000, 30000),
                                                  (25, 200000, 40000), (26, 190000, 35000), (27, 170000, 28000),
                                                  (28, 140000, 20000), (29, 130000, 18000), (30, 150000, 22000),
                                                  (31, 100000, 10000);