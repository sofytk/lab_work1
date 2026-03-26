-- Таблица работников
CREATE TABLE person (
                        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        full_name VARCHAR(255),
                        age SMALLINT CHECK ( age BETWEEN 16 AND 75)
);

-- Таблица проектов
CREATE TABLE project (
                         id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         project_name VARCHAR(255),
                         description TEXT,
                         tech_stack TEXT[]
);

-- Таблица связывающая людей и проекты, в которых они работают
CREATE TABLE project_member (
                                id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                person_id INT REFERENCES person(id),
                                project_id INT REFERENCES project(id),
                                role_name VARCHAR(100)
);


INSERT INTO person (full_name, age) VALUES
                                        ('Иванов Иван Иванович', 25),
                                        ('Петров Петр Петрович', 30),
                                        ('Смирнова Анна Сергеевна', 28),
                                        ('Кузнецов Алексей Викторович', 35),
                                        ('Попова Ольга Андреевна', 32),
                                        ('Соколов Дмитрий Алексеевич', 40),
                                        ('Морозова Марина Сергеевна', 27),
                                        ('Васильев Сергей Петрович', 45),
                                        ('Новикова Елена Ивановна', 29),
                                        ('Федоров Николай Андреевич', 38),
                                        ('Семенова Наталья Юрьевна', 31),
                                        ('Михайлов Андрей Викторович', 33),
                                        ('Громова Виктория Сергеевна', 26),
                                        ('Зайцев Владимир Николаевич', 42),
                                        ('Лебедева Татьяна Алексеевна', 36),
                                        ('Крылов Олег Сергеевич', 39),
                                        ('Кузнецова Анастасия Владимировна', 24),
                                        ('Тимофеев Игорь Анатольевич', 41);

-- Добавляем проекты
    INSERT INTO project (project_name, description, tech_stack) VALUES
    ('Мобильное приложение', 'Разработка Android приложения', ARRAY['Java','Kotlin','Firebase']),
    ('Веб-платформа', 'Веб-сервис для аналитики', ARRAY['Python','Django','PostgreSQL']),
    ('CRM система', 'Система управления клиентами', ARRAY['C#','ASP.NET','SQL Server']),
    ('E-commerce платформа', 'Интернет-магазин', ARRAY['PHP','Laravel','MySQL']),
    ('Data Pipeline', 'Обработка больших данных', ARRAY['Python','Spark','Hadoop']);

-- Добавляем людей в проекты
INSERT INTO project_member (person_id, project_id, role_name) VALUES
                                                                  (1,1,'Разработчик мобильного приложения'),
                                                                  (2,1,'Тестировщик'),
                                                                  (3,2,'Frontend-разработчик'),
                                                                  (4,2,'Backend-разработчик'),
                                                                  (5,3,'Аналитик'),
                                                                  (6,3,'Разработчик CRM'),
                                                                  (7,4,'Frontend-разработчик'),
                                                                  (8,4,'Backend-разработчик'),
                                                                  (9,5,'Data Engineer'),
                                                                  (10,5,'Data Analyst'),
                                                                  (11,1,'Разработчик мобильного приложения'),
                                                                  (12,2,'Backend-разработчик'),
                                                                  (13,3,'Аналитик'),
                                                                  (14,4,'Разработчик CRM'),
                                                                  (15,5,'Data Engineer'),
                                                                  (16,1,'Тестировщик'),
                                                                  (17,2,'Frontend-разработчик'),
                                                                  (18,3,'Backend-разработчик');


