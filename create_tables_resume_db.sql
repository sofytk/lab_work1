-- Таблица работников
CREATE TABLE person (
                        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        full_name VARCHAR(255),
                        age SMALLINT CHECK ( age BETWEEN 16 AND 75)

);
-- Таблица с резюме работников
CREATE TABLE resume (
                        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        person_id INT REFERENCES person(id),
                        start_work DATE,
                        skills TEXT[]
);
-- Таблица с зарплатой работников
CREATE TABLE salary (
                        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        person_id INT REFERENCES person(id),
                        salary INT,
                        bonus INT
);


-- Добавляем работников с видоизменёнными ФИО
INSERT INTO person (full_name, age) VALUES
                                        ('иванов иван иванович',25),
                                        ('пЕТРОВ Петр Петрович',30),
                                        ('смирнова анна сергеевна',28),
                                        ('кузнецов алексей викторович',35),
                                        ('попова ольга андреевна',32),
                                        ('соколов дмитрИЙ алексеевич',40),
                                        ('морозова марина сергеевна',27),
                                        ('Васильев сергей петрович',45),
                                        ('новикова Елена ивановна',29),
                                        ('федоров ниКолай андреевич',38),
                                        ('семенова Наталья юрьевнА',31),
                                        ('михайлов андрей викторович',33),
                                        ('громова виктория сергеевна',26),
                                        ('зайцев владимир НиколаевиЧ',42),
                                        ('лебедева татьяна алексеевна',36),
                                        ('крылов ОЛЕГ сергеевич',39),
                                        ('кузнецова анастасия владимировна',24),
                                        ('тимофеев игорь анатольевич',41);

-- Добавляем работников с видоизменёнными ФИО
INSERT INTO resume (person_id, start_work, skills) VALUES
                                                       (1,'2015-06-01',ARRAY['Java','Kotlin','Git']),
                                                       (2,'2010-09-15',ARRAY['Python','Testing','SQL']),
                                                       (3,'2018-01-20',ARRAY['HTML','CSS','JavaScript']),
                                                       (4,'2012-05-10',ARRAY['C#','SQL','Git']),
                                                       (5,'2014-07-22',ARRAY['PHP','Laravel','MySQL']),
                                                       (6,'2008-03-18',ARRAY['Python','Spark','Hadoop']),
                                                       (7,'2017-11-05',ARRAY['Java','Android','Firebase']),
                                                       (8,'2005-12-25',ARRAY['C#','ASP.NET','SQL Server']),
                                                       (9,'2016-02-14',ARRAY['HTML','CSS','JS']),
                                                       (10,'2011-09-19',ARRAY['Python','Django','PostgreSQL']),
                                                       (11,'2013-06-03',ARRAY['Java','Spring','Hibernate']),
                                                       (12,'2010-08-20',ARRAY['Python','Flask','SQL']),
                                                       (13,'2015-09-15',ARRAY['HTML','CSS','React']),
                                                       (14,'2009-07-30',ARRAY['C#','ASP.NET','MVC']),
                                                       (15,'2012-03-12',ARRAY['Python','Pandas','NumPy']),
                                                       (16,'2011-05-06',ARRAY['Java','Kotlin','Git']),
                                                       (17,'2010-11-11',ARRAY['JavaScript','React','Node']),
                                                       (18,'2013-01-25',ARRAY['C#','SQL Server','Git']);

-- Добавляем данные о зп
INSERT INTO salary (person_id, salary, bonus) VALUES
                                                  (1,120000,15000),
                                                  (2,200000,30000),
                                                  (3,90000,10000),
                                                  (4,150000,20000),
                                                  (5,110000,12000),
                                                  (6,180000,25000),
                                                  (7,125000,16000),
                                                  (8,210000,35000),
                                                  (9,95000,9000),
                                                  (10,160000,22000),
                                                  (11,130000,15000),
                                                  (12,170000,24000),
                                                  (13,100000,11000),
                                                  (14,155000,21000),
                                                  (15,140000,18000),
                                                  (16,120000,15000),
                                                  (17,145000,17000),
                                                  (18,160000,22000);
