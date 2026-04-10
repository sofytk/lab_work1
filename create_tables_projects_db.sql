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

-- Таблица связывающая людей и проекты, в которых они работают
CREATE TABLE project_member
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id  INT REFERENCES person (id),
    project_id INT REFERENCES project (id),
    role_name  VARCHAR(100)
);

INSERT INTO person (full_name, external_id, age)
VALUES
-- Одинаковые ФИО, разные люди (разные UUID)
('Иванов Иван Иванович', '11111111-1111-1111-1111-111111111111', 25),
('Иванов Иван Иванович', '22222222-2222-2222-2222-222222222222', 30),
('Петров Петр Петрович', '33333333-3333-3333-3333-333333333333', 30),
('Смирнова Анна Сергеевна', '44444444-4444-4444-4444-444444444444', 28),
('Кузнецов Алексей Викторович', '55555555-5555-5555-5555-555555555555', 35),
('Попова Ольга Андреевна', '66666666-6666-6666-6666-666666666666', 32),
('Соколов Дмитрий Алексеевич', '77777777-7777-7777-7777-777777777777', 40),
('Морозова Марина Сергеевна', '88888888-8888-8888-8888-888888888888', 27),
('Васильев Сергей Петрович', '99999999-9999-9999-9999-999999999999', 45),
('Новикова Елена Ивановна', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 29),
('Федоров Николай Андреевич', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 38),
('Семенова Наталья Юрьевна', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 31),
('Михайлов Андрей Викторович', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 33),
('Громова Виктория Сергеевна', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 26),
('Зайцев Владимир Николаевич', 'ffffffff-ffff-ffff-ffff-ffffffffffff', 42),
('Лебедева Татьяна Алексеевна', '12121212-1212-1212-1212-121212121212', 36),
('Крылов Олег Сергеевич', '13131313-1313-1313-1313-131313131313', 39),
('Кузнецова Анастасия Владимировна', '14141414-1414-1414-1414-141414141414', 24),
('Тимофеев Игорь Анатольевич', '15151515-1515-1515-1515-151515151515', 41);

-- Добавляем проекты
INSERT INTO project (project_name, description, tech_stack)
VALUES ('Мобильное приложение', 'Разработка Android приложения', ARRAY ['Java','Kotlin','Firebase']),
       ('Веб-платформа', 'Веб-сервис для аналитики', ARRAY ['Python','Django','PostgreSQL']),
       ('CRM система', 'Система управления клиентами', ARRAY ['C#','ASP.NET','SQL Server']),
       ('E-commerce платформа', 'Интернет-магазин', ARRAY ['PHP','Laravel','MySQL']),
       ('Data Pipeline', 'Обработка больших данных', ARRAY ['Python','Spark','Hadoop']);

-- Добавляем людей в проекты
INSERT INTO project_member (person_id, project_id, role_name)
VALUES (1, 1, 'Разработчик мобильного приложения'),
       (2, 1, 'Тестировщик'),
       (3, 2, 'Frontend-разработчик'),
       (4, 2, 'Backend-разработчик'),
       (5, 3, 'Аналитик'),
       (6, 3, 'Разработчик CRM'),
       (7, 4, 'Frontend-разработчик'),
       (8, 4, 'Backend-разработчик'),
       (9, 5, 'Data Engineer'),
       (10, 5, 'Data Analyst'),
       (11, 1, 'Разработчик мобильного приложения'),
       (12, 2, 'Backend-разработчик'),
       (13, 3, 'Аналитик'),
       (14, 4, 'Разработчик CRM'),
       (15, 5, 'Data Engineer'),
       (16, 1, 'Тестировщик'),
       (17, 2, 'Frontend-разработчик'),
       (18, 3, 'Backend-разработчик');