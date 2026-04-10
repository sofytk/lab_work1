-- Таблица работников
CREATE TABLE person
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name   VARCHAR(255),
    external_id UUID UNIQUE,
    age         SMALLINT CHECK ( age BETWEEN 16 AND 75)

);

-- Таблица с резюме работников
CREATE TABLE resume
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id  INT REFERENCES person (id),
    start_work DATE,
    skills     TEXT[]
);

-- Таблица с зарплатой работников
CREATE TABLE salary
(
    id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id INT REFERENCES person (id),
    salary    INT,
    bonus     INT
);

-- Добавляем работников
INSERT INTO person (full_name, external_id, age)
VALUES ('Иванов Иван Иванович', '11111111-1111-1111-1111-111111111111', 25),
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

-- Добавляем резюме
INSERT INTO resume (person_id, start_work, skills)
VALUES (1, '2015-06-01', ARRAY ['Java','Kotlin','Git']),
       (2, '2010-09-15', ARRAY ['Python','Testing','SQL']),
       (3, '2018-01-20', ARRAY ['HTML','CSS','JavaScript']),
       (4, '2012-05-10', ARRAY ['C#','SQL','Git']),
       (5, '2014-07-22', ARRAY ['PHP','Laravel','MySQL']),
       (6, '2008-03-18', ARRAY ['Python','Spark','Hadoop']),
       (7, '2017-11-05', ARRAY ['Java','Android','Firebase']),
       (8, '2005-12-25', ARRAY ['C#','ASP.NET','SQL Server']),
       (9, '2016-02-14', ARRAY ['HTML','CSS','JS']),
       (10, '2011-09-19', ARRAY ['Python','Django','PostgreSQL']),
       (11, '2013-06-03', ARRAY ['Java','Spring','Hibernate']),
       (12, '2010-08-20', ARRAY ['Python','Flask','SQL']),
       (13, '2015-09-15', ARRAY ['HTML','CSS','React']),
       (14, '2009-07-30', ARRAY ['C#','ASP.NET','MVC']),
       (15, '2012-03-12', ARRAY ['Python','Pandas','NumPy']),
       (16, '2011-05-06', ARRAY ['Java','Kotlin','Git']),
       (17, '2010-11-11', ARRAY ['JavaScript','React','Node']),
       (18, '2013-01-25', ARRAY ['C#','SQL Server','Git']);

-- Добавляем зарплаты
INSERT INTO salary (person_id, salary, bonus)
VALUES (1, 120000, 15000),
       (2, 200000, 30000),
       (3, 90000, 10000),
       (4, 150000, 20000),
       (5, 110000, 12000),
       (6, 180000, 25000),
       (7, 125000, 16000),
       (8, 210000, 35000),
       (9, 95000, 9000),
       (10, 160000, 22000),
       (11, 130000, 15000),
       (12, 170000, 24000),
       (13, 100000, 11000),
       (14, 155000, 21000),
       (15, 140000, 18000),
       (16, 120000, 15000),
       (17, 145000, 17000),
       (18, 160000, 22000);
